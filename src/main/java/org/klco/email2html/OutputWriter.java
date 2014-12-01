/*
 * Copyright (C) 2012 Dan Klco
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in 
 * the Software without restriction, including without limitation the rights to 
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 * of the Software, and to permit persons to whom the Software is furnished to do 
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
package org.klco.email2html;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;

import javax.mail.MessagingException;
import javax.mail.Part;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.models.EmailMessage;
import org.klco.email2html.models.Rendition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for writing output from the email messages to the filesystem.
 * 
 * @author dklco
 */
public class OutputWriter {

	/** The Constant FILE_DATE_FORMAT. */
	private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");

	/** The Constant log. */
	private static final Logger log = LoggerFactory
			.getLogger(OutputWriter.class);

	/**
	 * A set of the downloaded attachment checksums, can ensure each attachment
	 * is only downloaded once
	 */
	private Set<Long> attachmentChecksums = new HashSet<Long>();

	/**
	 * A flag for excluding duplicates, loaded from the configuration
	 */
	private boolean excludeDuplicates;

	/** The output dir. */
	private File outputDir;

	/**
	 * The image renditions to create
	 */
	private Rendition[] renditions;

	/**
	 * The template content
	 */
	private String template;

	private Email2HTMLConfiguration config;

	/**
	 * Constructs a new OutputWriter.
	 * 
	 * @param config
	 *            the current configuration
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public OutputWriter(Email2HTMLConfiguration config)
			throws FileNotFoundException, IOException {
		log.trace("HTMLWriter");

		outputDir = new File(config.getOutputDir());
		log.info("Using output directory {}", outputDir.getAbsolutePath());
		if (!outputDir.exists()) {
			log.info("Creating ouput directory");
			outputDir.mkdirs();
		}
		if(StringUtils.isEmpty(config.getTemplate())){
			throw new FileNotFoundException("No template file specified");
		}
		template = IOUtils.toString(
				new FileInputStream(new File(config.getTemplate())), "UTF-8");

		this.renditions = config.getRenditions();

		this.excludeDuplicates = config.isExcludeDuplicates();
		this.config = config;
	}

	/**
	 * Adds the attachment to the EmailMessage. Call this method when the email
	 * content has most likely already been loaded.
	 * 
	 * @param containingMessage
	 *            the Email Message to add the attachment to
	 * @param part
	 *            the content of the attachment
	 * @throws IOException
	 * @throws MessagingException
	 */
	public void addAttachment(EmailMessage containingMessage, Part part)
			throws IOException, MessagingException {
		log.trace("addAttachment");

		File attachmentFolder = new File(outputDir.getAbsolutePath()
				+ File.separator + config.getImagesSubDir() + File.separator
				+ FILE_DATE_FORMAT.format(containingMessage.getSentDate()));
		File attachmentFile = new File(attachmentFolder, part.getFileName());

		boolean addAttachment = true;
		boolean writeAttachment = false;
		if (!attachmentFolder.exists() || !attachmentFile.exists()) {
			log.warn("Attachment or folder missing, writing attachment {}",
					attachmentFile.getName());
			writeAttachment = true;
		}

		if (!writeAttachment
				&& part.getContentType().toLowerCase().startsWith("image")) {
			for (Rendition rendition : renditions) {
				File renditionFile = new File(attachmentFolder,
						rendition.getName() + "-" + part.getFileName());
				if (!renditionFile.exists()) {
					log.warn("Rendition {} missing, writing attachment {}",
							renditionFile.getName(), attachmentFile.getName());
					writeAttachment = true;
					break;
				}
			}
		}
		if (writeAttachment) {
			addAttachment = writeAttachment(containingMessage, part);
		} else {
			if (this.excludeDuplicates) {
				log.debug("Computing checksum");
				InputStream is = null;
				try {
					CRC32 checksum = new CRC32();
					is = new BufferedInputStream(new FileInputStream(
							attachmentFile));
					for (int read = is.read(); read != -1; read = is.read()) {
						checksum.update(read);
					}
					long value = checksum.getValue();
					if (attachmentChecksums.contains(value)) {
						addAttachment = false;
					} else {
						attachmentChecksums.add(checksum.getValue());
					}
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		}
		if (addAttachment) {
			containingMessage.getAttachments().add(attachmentFile);
		} else {
			log.debug("Attachment is a duplicate, not adding as message attachment");
		}
	}

	/**
	 * Checks to see if a file exists for the specified message.
	 * 
	 * @param emailMessage
	 *            the message to check
	 * @return true if a file exists, false otherwise
	 */
	public boolean fileExists(EmailMessage emailMessage) {
		File messageFile = new File(outputDir.getAbsolutePath()
				+ File.separator
				+ FILE_DATE_FORMAT.format(emailMessage.getSentDate()) + ".html");
		return messageFile.exists();
	}

	/**
	 * Writes the attachment contained in the body part to a file.
	 * 
	 * @param containingMessage
	 *            the message this body part is contained within
	 * @param part
	 *            the part containing the attachment
	 * @return the file that was created/written to
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *             the messaging exception
	 */
	public boolean writeAttachment(EmailMessage containingMessage, Part part)
			throws IOException, MessagingException {
		log.trace("writeAttachment");

		File attachmentFolder;
		File attachmentFile;
		InputStream in = null;
		OutputStream out = null;
		try {

			attachmentFolder = new File(outputDir.getAbsolutePath()
					+ File.separator + config.getImagesSubDir()
					+ File.separator
					+ FILE_DATE_FORMAT.format(containingMessage.getSentDate()));
			if (!attachmentFolder.exists()) {
				log.debug("Creating attachment folder");
				attachmentFolder.mkdirs();
			}

			attachmentFile = new File(attachmentFolder, part.getFileName());
			log.debug("Writing attachment file: {}",
					attachmentFile.getAbsolutePath());
			if (!attachmentFile.exists()) {
				attachmentFile.createNewFile();
			}

			in = new BufferedInputStream(part.getInputStream());
			out = new BufferedOutputStream(new FileOutputStream(attachmentFile));

			log.debug("Downloading attachment");
			CRC32 checksum = new CRC32();
			for (int b = in.read(); b != -1; b = in.read()) {
				checksum.update(b);
				out.write(b);
			}

			if (this.excludeDuplicates) {
				log.debug("Computing checksum");
				long value = checksum.getValue();
				if (this.attachmentChecksums.contains(value)) {
					log.info("Skipping duplicate attachment: {}",
							part.getFileName());
					attachmentFile.delete();
					return false;
				} else {
					attachmentChecksums.add(value);
				}
			}

			log.debug("Attachement saved");
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}

		if (part.getContentType().toLowerCase().startsWith("image")) {
			log.debug("Creating renditions");
			String contentType = part.getContentType().substring(0,
					part.getContentType().indexOf(";"));
			log.debug("Creating renditions of type: " + contentType);

			for (Rendition rendition : renditions) {
				File renditionFile = new File(attachmentFolder,
						rendition.getName() + "-" + part.getFileName());
				try {
					if (!renditionFile.exists()) {
						renditionFile.createNewFile();
					}
					log.debug("Creating rendition file: {}",
							renditionFile.getAbsolutePath());
					createRendition(attachmentFile, renditionFile, rendition);
					log.debug("Rendition created");
				} catch (OutOfMemoryError oome) {
					Runtime rt = Runtime.getRuntime();
					rt.gc();
					log.warn("Ran out of memory creating rendition: "
							+ rendition, oome);

					log.warn("Free Memory: {}", rt.freeMemory());
					log.warn("Max Memory: {}", rt.maxMemory());
					log.warn("Total Memory: {}", rt.totalMemory());

					String[] command = null;
					if (rendition.getFill()) {
						command = new String[] {
								"convert",
								attachmentFile.getAbsolutePath(),
								"-resize",
								(rendition.getHeight() * 2) + "x",
								"-resize",
								"'x" + (rendition.getHeight() * 2) + "<'",
								"-resize",
								"50%",
								"-gravity",
								"center",
								"-crop",
								rendition.getHeight() + "x"
										+ rendition.getWidth() + "+0+0",
								"+repage",
								renditionFile.getAbsolutePath() };
					} else {
						command = new String[] {
								"convert",
								attachmentFile.getAbsolutePath(),
								"-resize",
								rendition.getHeight() + "x"
										+ rendition.getWidth(),
								renditionFile.getAbsolutePath() };

					}
					log.debug("Trying to resize with ImageMagick: "
							+ StringUtils.join(command, " "));

					rt.exec(command);
				} catch (Exception t) {
					log.warn("Exception creating rendition: " + rendition, t);
				}
			}
		}
		return true;
	}

	private void createRendition(File originalFile, File renditionFile,
			Rendition rendition) throws IOException {
		if (!renditionFile.exists()) {
			renditionFile.createNewFile();
		}
		log.debug("Creating rendition file: {}",
				renditionFile.getAbsolutePath());
		if (rendition.getFill()) {
			log.debug("Adding fill");
			Thumbnails
					.of(originalFile)
					.size(rendition.getWidth(), rendition.getHeight())
					.addFilter(
							new Canvas(rendition.getWidth(), rendition
									.getHeight(), Positions.CENTER, Color.WHITE))
					.toFile(renditionFile);
		} else {
			Thumbnails.of(originalFile)
					.size(rendition.getWidth(), rendition.getHeight())
					.toFile(renditionFile);
		}
	}

	/**
	 * Writes the message to a html file. The name of the HTML file is generated
	 * from the date of the message.
	 * 
	 * @param emailMessage
	 *            the message to save to a file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeHTML(EmailMessage emailMessage) throws IOException {
		log.trace("writeHTML");

		Map<String, Object> params = emailMessage.toMap();
		params.put(
				"attachmentFolder",
				config.getImagesSubDir() + File.separator
						+ FILE_DATE_FORMAT.format(emailMessage.getSentDate()));
		if (config.getHookObj() != null) {
			config.getHookObj().beforeWrite(emailMessage, params);
		}

		File messageFolder = new File(outputDir.getAbsolutePath()
				+ File.separator + config.getMessagesSubDir());
		if (!messageFolder.exists()) {
			log.debug("Creating messages folder");
			messageFolder.mkdirs();
		}

		StrSubstitutor sub = new StrSubstitutor(params);
		String fileContent = sub.replace(template);

		String name = String.format(config.getFileNameFormat(),
				emailMessage.getSentDate(), toPath(emailMessage.getSubject()));
		File messageFile = new File(outputDir.getAbsolutePath()
				+ File.separator + config.getMessagesSubDir() + File.separator
				+ name);

		OutputStream os = null;
		try {
			os = new FileOutputStream(messageFile);
			IOUtils.write(fileContent, new FileOutputStream(messageFile));
			log.debug("Writing message to file {}",
					messageFile.getAbsolutePath());
		} finally {
			IOUtils.closeQuietly(os);
		}
		if (config.getHookObj() != null) {
			config.getHookObj().afterWrite(emailMessage, params, messageFile);
		}
	}

	/**
	 * Converts the specified name or arbitrary string into a path by downcasing
	 * it and replacing all non-ASCII characters with dashes.
	 * 
	 * @param str
	 *            the string to convert
	 * @return the path
	 */
	public static final String toPath(final String str) {
		if (str == null) {
			return null;
		}
		boolean prev = false;
		final StringBuilder sb = new StringBuilder();
		for (char c : Normalizer.normalize(str.toLowerCase(),
				Normalizer.Form.NFKD).toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				sb.append(c);
				prev = true;
			} else if (prev) {
				sb.append('-');
				prev = false;
			}
		}
		if (sb.toString().endsWith("-")) {
			return sb.substring(0, sb.length() - 1);
		} else {
			return sb.toString();
		}
	}
}
