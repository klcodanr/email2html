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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.models.EmailMessage;
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

	/** The output dir. */
	private File outputDir;

	/** The template. */
	private Template template;

	private Template indexTemplate;

	/** The Constant log. */
	private static final Logger log = LoggerFactory
			.getLogger(OutputWriter.class);

	/**
	 * Constructs a new OutputWriter.
	 * 
	 * @param config
	 *            the current configuration
	 */
	public OutputWriter(Email2HTMLConfiguration config) {
		log.trace("HTMLWriter");

		outputDir = new File(config.getOutputDir());
		if (!outputDir.exists()) {
			log.info("Creating ouput directory");
			outputDir.mkdirs();
		}

		log.debug("Initializing templating engine");
		Velocity.setProperty("file.resource.loader.path",
				config.getTemplateDir());
		Velocity.init();

		log.debug("Initializing template template.vm");
		template = Velocity.getTemplate("template.vm");

		log.debug("Initializing index template: index.vm");
		indexTemplate = Velocity.getTemplate("index.vm");
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

		log.debug("Initializing templating context");
		VelocityContext context = new VelocityContext();
		context.put("emailMessage", emailMessage);

		File messageFile = new File(outputDir.getAbsolutePath()
				+ File.separator
				+ FILE_DATE_FORMAT.format(emailMessage.getSentDate()) + ".html");
		log.debug("Writing message to file {}", messageFile.getAbsolutePath());

		writeHTML(messageFile, context, template);
	}

	/**
	 * Writes the content from the merging of the context and template to the
	 * specified file.
	 * 
	 * @param messageFile
	 *            the file to save the contents
	 * @param context
	 *            the context containing all of the properties to save
	 * @param template
	 *            the velocity template to use
	 * @throws IOException
	 */
	private void writeHTML(File messageFile, VelocityContext context,
			Template template) throws IOException {
		log.trace("writeHTML");

		if (!messageFile.exists()) {
			log.debug("Creating message file");
			messageFile.createNewFile();
		}
		log.debug("Merging message into template");
		StringWriter sw = new StringWriter();
		template.merge(context, sw);

		log.debug("Writing contents to file");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(messageFile);
			IOUtils.copy(
					new ByteArrayInputStream(sw.toString().getBytes("UTF-8")),
					fos);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Writes the index file to the filesystem
	 * 
	 * @param messages
	 *            the messages for the index file to be generated from.
	 * @throws IOException
	 */
	public void writeIndex(List<EmailMessage> messages) throws IOException {
		log.trace("writeHTML");

		log.debug("Initializing templating context");
		VelocityContext context = new VelocityContext();
		context.put("messages", messages);

		File messageFile = new File(outputDir.getAbsolutePath()
				+ File.separator + "index.html");
		log.debug("Writing message to file {}", messageFile.getAbsolutePath());

		writeHTML(messageFile, context, indexTemplate);
	}

	/**
	 * Writes the attachment contained in the body part to a file.
	 * 
	 * @param containingMessage
	 *            the message this body part is contained within
	 * @param bodyPart
	 *            the part containing the attachment
	 * @return the file that was created/written to
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *             the messaging exception
	 */
	public void writeAttachment(EmailMessage containingMessage,
			BodyPart bodyPart) throws IOException, MessagingException {
		log.trace("writeAttachment");

		File imageFolder = new File(outputDir.getAbsolutePath()
				+ File.separator
				+ FILE_DATE_FORMAT.format(containingMessage.getSentDate()));

		if (!imageFolder.exists()) {
			log.debug("Creating image folder");
			imageFolder.mkdirs();
		}
		log.debug("Writing image file: " + bodyPart.getFileName());
		File imageFile = new File(imageFolder, bodyPart.getFileName());
		imageFile.createNewFile();
		OutputStream os = null;
		try {
			log.debug("Writing to file");
			os = new FileOutputStream(imageFile);
			IOUtils.copy(bodyPart.getInputStream(), os);
			containingMessage.getAttachments().add(imageFile);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
}
