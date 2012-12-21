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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.ParseException;
import javax.mail.search.SubjectTerm;

import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.models.EmailMessage;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for reading emails from an email server.
 * 
 * @author dklco
 */
public class EmailReader {

	/**
	 * Constant for application content types.
	 */
	public static final String CT_PT_APPLICATION = "application";

	/**
	 * Constant for audio content types.
	 */
	public static final String CT_PT_AUDIO = "audio";

	/**
	 * Constant for image content types.
	 */
	public static final String CT_PT_IMAGE = "image";

	/**
	 * Constant for multipart content types.
	 */
	public static final String CT_PT_MULTIPART = "multipart";

	/**
	 * Constant for text content types.
	 */
	public static final String CT_PT_TEXT = "text";

	/**
	 * Constant for video content types.
	 */
	public static final String CT_PT_VIDEO = "video";

	/**
	 * Constant for HTML secondary content type.
	 */
	public static final String CT_ST_HTML = "html";

	/** The Constant log. */
	private static final Logger log = LoggerFactory
			.getLogger(EmailReader.class);

	/**
	 * The HTML Sanitizer policy, essentially allows only block level elements,
	 */
	private static final PolicyFactory policy = Sanitizers.FORMATTING
			.and(Sanitizers.BLOCKS).and(Sanitizers.IMAGES)
			.and(Sanitizers.LINKS);

	/** The Constant READABLE_DATE_FORMAT. */
	private static final SimpleDateFormat READABLE_DATE_FORMAT = new SimpleDateFormat(
			"MMM d, yyyy");

	/**
	 * Gets the sender.
	 * 
	 * @param message
	 *            the message
	 * @return the sender
	 */
	private static String getSender(Message message) {
		log.trace("getSender");
		String from = "";
		try {
			log.debug("Getting sender");
			InternetAddress address = (InternetAddress) message.getFrom()[0];
			if (address.getPersonal() != null
					&& address.getPersonal().trim().length() != 0) {
				from = address.getPersonal();
			} else {
				from = address.getAddress().substring(0,
						address.getAddress().indexOf("@"));
			}
		} catch (Exception e) {
			log.warn("Unable to get address", e);
		}
		return from;
	}

	private String[] breakStrings;

	/** The config. */
	private Email2HTMLConfiguration config;

	/**
	 * The OutputWriter instance.
	 */
	private OutputWriter outputWriter = null;

	private boolean overwrite;

	/**
	 * Instantiates a new email reader.
	 * 
	 * @param config
	 *            the config
	 */
	public EmailReader(Email2HTMLConfiguration config) {
		this.config = config;
		outputWriter = new OutputWriter(config);

		overwrite = Boolean.valueOf(config.getOverwrite());

		breakStrings = config.getBreakStrings().split("\\,");
		log.debug("Using break strings: " + Arrays.toString(breakStrings));
	}

	/**
	 * Handles multiparts, will iterate through all the parts and attempt to
	 * retrieve the message contents.
	 * 
	 * @param message
	 *            the email message object
	 * @param multipart
	 *            the multipart container
	 * @throws MessagingException
	 */
	private void getMessageContent(EmailMessage message, Multipart multipart)
			throws MessagingException {
		log.trace("getMessageContent(Multipart)");
		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart bodyPart = multipart.getBodyPart(i);
			try {
				getMessageContent(message, bodyPart);
			} catch (Exception e) {
				log.warn("Exception processing part " + i + "of type "
						+ bodyPart.getContentType(), e);
			}
		}

	}

	/**
	 * Handles the individual parts, attempts to figure out what each part is
	 * and handle it appropriately.
	 * 
	 * @param message
	 *            the message object to populate
	 * @param part
	 *            the current message part
	 * @throws ParseException
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void getMessageContent(EmailMessage message, Part part)
			throws ParseException, MessagingException, IOException {
		log.trace("getMessageContent(Part)");
		ContentType contentType = new ContentType(part.getContentType());

		log.debug("Processing part of primary type {} and sub type {}",
				contentType.getPrimaryType(), contentType.getSubType());
		if (CT_PT_TEXT.equalsIgnoreCase(contentType.getPrimaryType())) {
			log.debug("Processing text part");
			getText(message, part);
		}  else if (CT_PT_MULTIPART.equalsIgnoreCase(contentType.getPrimaryType())) {
			log.debug("Handling multipart");
			getMessageContent(message, (Multipart) part.getContent());
		} else if (CT_PT_IMAGE.equalsIgnoreCase(contentType.getPrimaryType())
				|| CT_PT_VIDEO.equalsIgnoreCase(contentType.getPrimaryType())
				|| CT_PT_AUDIO.equalsIgnoreCase(contentType.getPrimaryType())
				|| CT_PT_APPLICATION.equalsIgnoreCase(contentType.getPrimaryType())) {
			log.debug("Handling attachment");
			outputWriter.addAttachment(message, part);
		}else {
			log.warn("Unexpected primary type {} for content type {}",
					contentType.getPrimaryType(), part.getContentType());
		}
	}

	/**
	 * Get the text from the part.
	 * 
	 * @param message
	 *            the message to update
	 * @param part
	 *            the part from which to retrieve the text
	 * @throws ParseException
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void getText(EmailMessage message, Part part)
			throws ParseException, MessagingException, IOException {
		log.trace("getText");
		ContentType contentType = new ContentType(part.getContentType());
		if (message.getFullMessage() == null
				|| CT_ST_HTML.equalsIgnoreCase(contentType.getSubType())) {
			log.debug("Loading text of type: {}", contentType.getSubType());
			message.setFullMessage(part.getContent().toString());
			message.setMessage(policy.sanitize(trimMessage(message
					.getFullMessage())));
		}
	}

	/**
	 * Read emails.
	 */
	public void readEmails() {
		log.info("getEmail");
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		try {

			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			log.info("Connecting to {} with user {}", config.getUrl(),
					config.getUsername());
			store.connect(config.getUrl(), config.getUsername(),
					config.getPassword());
			Folder folder = store.getFolder(config.getFolder());
			folder.open(Folder.READ_ONLY);

			Message[] messages = null;
			if (config.getSearchSubject() != null) {
				log.debug("Searching for messages with subject {}",
						config.getSearchSubject());
				SubjectTerm subjectTerm = new SubjectTerm(
						config.getSearchSubject());
				messages = folder.search(subjectTerm);
			} else {
				messages = folder.getMessages();
			}

			List<EmailMessage> sortedMessages = new ArrayList<EmailMessage>();
			log.debug("Loading messages from the server");
			for (int i = 0; i < messages.length; i++) {
				log.info("Processing message {} of {}", i, messages.length);
				Message message = messages[i];
				try {
					sortedMessages.add(saveMessage(message));
				} catch (Exception e) {
					log.error(
							"Exception saving message: "
									+ READABLE_DATE_FORMAT.format(message
											.getSentDate()), e);
				}
			}

			log.debug("Sorting messages");
			Collections.sort(sortedMessages, new Comparator<EmailMessage>() {
				public int compare(EmailMessage o1, EmailMessage o2) {
					return o1.getSentDate().compareTo(o2.getSentDate());
				}
			});

			log.debug("Writing index file");
			outputWriter.writeIndex(sortedMessages);

		} catch (MessagingException e) {
			log.error("Exception accessing emails", e);
		} catch (IOException e) {
			log.error("IOException accessing emails", e);
		}
	}

	/**
	 * Save message.
	 * 
	 * @param message
	 *            the message
	 * @param outputDir
	 *            the output dir
	 * @param templateStr
	 *            the template str
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *             the messaging exception
	 */
	private EmailMessage saveMessage(Message message) throws IOException,
			MessagingException {
		log.trace("saveMessage");

		log.debug("Processing message from: " + message.getSentDate());

		log.debug("Loading default properties");
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setSubject(message.getSubject());
		emailMessage.setSender(getSender(message));
		emailMessage.setSentDate(message.getSentDate());

		log.debug("Loading message content");

		getMessageContent(emailMessage, message);

		boolean alreadyExists = outputWriter.fileExists(emailMessage);
		if (overwrite || !alreadyExists) {
			outputWriter.writeHTML(emailMessage);
		} else {
			log.debug("Message already exists, not writing");
		}
		return emailMessage;
	}

	/**
	 * Trim message.
	 * 
	 * @param message
	 *            the message
	 * @return the string
	 */
	private String trimMessage(String message) {
		log.trace("trimMessage");

		for (String breakString : breakStrings) {
			int index = message.indexOf(breakString);
			if (index != -1) {
				message = message.substring(0, index);
			}
		}
		return message;
	}
}
