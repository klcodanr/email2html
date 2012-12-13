package org.klco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SubjectTerm;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailReader {
	private static final Logger log = LoggerFactory
			.getLogger(EmailReader.class);

	private Properties properties;

	private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");

	private static final SimpleDateFormat READABLE_DATE_FORMAT = new SimpleDateFormat(
			"MMM d, yyyy");

	public EmailReader(Properties properties) {
		this.properties = properties;
	}

	public void readEmails(String password) {
		log.info("getEmail");
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
			log.info("Creating output dir");
			File outputDir = new File(properties.getProperty("output.dir"));
			if (!outputDir.exists()) {
				outputDir.mkdirs();
			}

			log.info("Loading template");
			InputStream in = EmailReader.class.getClassLoader()
					.getResourceAsStream(
							properties.getProperty("email.template.name"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			String templateStr = new String(baos.toByteArray(), "UTF-8");

			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect(properties.getProperty("email.url"),
					properties.getProperty("email.user"), password);
			Folder inbox = store.getFolder(properties
					.getProperty("email.folder"));
			inbox.open(Folder.READ_ONLY);

			SubjectTerm subjectTerm = new SubjectTerm(
					properties.getProperty("search.subject"));

			Queue<Message> messages = new LinkedList<Message>();
			for (Message message : inbox.search(subjectTerm)) {
				messages.add(message);
			}

			List<Message> sortedMessages = new ArrayList<Message>();
			log.debug("Found {} messages", messages.size());
			while (messages.peek() != null) {
				Message message = messages.poll();
				try {
					saveMessage(message, outputDir, templateStr);
				} catch (Exception e) {
					log.error(
							"Exception saving message: "
									+ FILE_DATE_FORMAT.format(message
											.getSentDate()), e);
				}
				sortedMessages.add(message);
			}

			Collections.sort(sortedMessages, new Comparator<Message>() {
				public int compare(Message o1, Message o2) {
					try {
						return o1.getSentDate().compareTo(o2.getSentDate());
					} catch (MessagingException e) {
						return -1;
					}
				}

			});
			StringBuffer body = new StringBuffer("<ul>");
			for (Message message : sortedMessages) {
				body.append("<li><a href=\""
						+ FILE_DATE_FORMAT.format(message.getSentDate())
						+ ".html\">From " + getSender(message) + " on "
						+ READABLE_DATE_FORMAT.format(message.getSentDate())
						+ "</a></li>");
			}
			body.append("</ul>");

			log.debug("Creating index file");
			String indexPage = templateStr.replace("{TITLE}",
					"Sarah Picture of the Day").replace("{BODY}",
					body.toString());

			File indexFile = new File(outputDir, "index.html");
			if (indexFile.exists()) {
				indexFile.createNewFile();

			}
			FileOutputStream fos = new FileOutputStream(indexFile);
			IOUtils.copy(new ByteArrayInputStream(indexPage.getBytes()), fos);
			IOUtils.closeQuietly(fos);

		} catch (MessagingException e) {
			log.error("Exception accessing emails", e);
		} catch (IOException e) {
			log.error("IOException accessing emails", e);
		}
	}

	private static String trimMessage(String message) {
		log.trace("trimMessage");
		if (message.contains("gmail_quote")) {
			message = message.substring(0, message.indexOf("gmail_quote") - 12);
		}

		if (message.contains("hr")) {
			message = message.substring(0, message.indexOf("hr") - 1);
		}
		return message;
	}

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
				from = address.getAddress();
			}
		} catch (Exception e) {
			log.warn("Unable to get address", e);
		}
		return from;
	}

	private void saveMessage(Message message, File outputDir, String templateStr)
			throws IOException, MessagingException {
		log.trace("saveMessage");

		log.debug("Processing message from: " + message.getSentDate());
		File messageFile = new File(outputDir.getAbsolutePath()
				+ File.separator
				+ FILE_DATE_FORMAT.format(message.getSentDate()) + ".html");

		log.debug("Output file: " + messageFile.getAbsolutePath());

		if (!messageFile.exists() || messageFile.length() == 0) {

			File imageFolder = new File(outputDir.getAbsolutePath()
					+ File.separator
					+ FILE_DATE_FORMAT.format(message.getSentDate()));

			Set<String> imageNames = new HashSet<String>();
			String body = null;

			if (message.getContent() instanceof MimeMultipart) {
				MimeMultipart parts = (MimeMultipart) message.getContent();
				for (int i = 0; i < parts.getCount(); i++) {
					BodyPart bodyPart = parts.getBodyPart(i);

					log.info("Found part: " + bodyPart.getContentType());

					if (bodyPart.getContentType().toUpperCase()
							.startsWith("IMAGE")) {
						if (!imageFolder.exists()) {
							log.debug("Creating image folder");
							imageFolder.mkdirs();
						}
						log.debug("Writing image file: "
								+ bodyPart.getFileName());
						File imageFile = new File(imageFolder,
								bodyPart.getFileName());
						imageFile.createNewFile();
						OutputStream os = new FileOutputStream(imageFile);
						IOUtils.copy(bodyPart.getInputStream(), os);
						IOUtils.closeQuietly(os);
						imageNames.add(imageFile.getName());
					} else {
						log.debug("Processing message text");
						if (bodyPart.getContent() instanceof MimeMultipart) {
							MimeMultipart textParts = (MimeMultipart) bodyPart
									.getContent();
							for (int d = 0; d < textParts.getCount(); d++) {
								BodyPart textPart = textParts.getBodyPart(d);

								if (textPart.getContentType().toLowerCase()
										.startsWith("text/html")
										|| (body == null && textPart
												.getContentType().toLowerCase()
												.startsWith("text/plain"))) {
									body = trimMessage((String) textPart
											.getContent());

								}
							}
						} else if (bodyPart.getContent() instanceof MimeBodyPart) {
							MimeBodyPart mimePart = (MimeBodyPart) bodyPart
									.getContent();
							if (mimePart.getContentType().toLowerCase()
									.startsWith("text/html")
									|| body == null) {
								body = trimMessage((String) mimePart
										.getContent());
							}
						} else {
							body = trimMessage(bodyPart.getContent().toString());

						}
					}
				}
			} else {
				body = trimMessage(message.getContent().toString());
			}

			if (!messageFile.exists()) {
				messageFile.createNewFile();
			}
			String template = templateStr
					.replace(
							"{TITLE}",
							"Sarah Picture of the Day: "
									+ READABLE_DATE_FORMAT.format(message
											.getSentDate()));

			log.debug("Creating image links");
			StringBuffer images = new StringBuffer("<ul id=\"images\">");
			for (String image : imageNames) {
				images.append("<li><img src=\"" + imageFolder.getName() + "/"
						+ image + "\"></li>");
			}
			images.append("</ul><p><a href=\"./index.html\"> &lt;&lt; Home</a></p>");

			body += "<p><strong>From: " + getSender(message) + "</strong></p>"
					+ images.toString();
			template = template
					.replace("{BODY}",
							"<p><a href=\"./index.html\"> &lt;&lt; Home</a></p>"
									+ body);

			FileOutputStream fos = new FileOutputStream(messageFile);
			IOUtils.copy(new ByteArrayInputStream(template.getBytes("UTF-8")),
					fos);
			IOUtils.closeQuietly(fos);
		} else {
			log.debug("Message already exists, skipping");
		}

	}
}
