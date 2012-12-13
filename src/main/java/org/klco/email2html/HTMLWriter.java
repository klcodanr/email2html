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
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.models.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLWriter {

	private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	private File outputDir;
	private Template template;
	private static final Logger log = LoggerFactory.getLogger(HTMLWriter.class);

	public HTMLWriter(Email2HTMLConfiguration config) {
		log.trace("HTMLWriter");

		outputDir = new File(config.getOutputDir());
		if (!outputDir.exists()) {
			log.info("Creating ouput directory");
			outputDir.mkdirs();
		}

		log.debug("Initializing templating engine");
		Velocity.init();

		log.debug("Initializing template from file {}", config.getTemplate());
		template = Velocity.getTemplate(config.getTemplate());
	}

	public boolean fileExists(EmailMessage emailMessage) {
		File messageFile = new File(outputDir.getAbsolutePath()
				+ File.separator
				+ FILE_DATE_FORMAT.format(emailMessage.getSentDate()) + ".html");
		return messageFile.exists();
	}

	public void writeHTML(EmailMessage emailMessage) throws IOException {
		log.trace("writeHTML");

		log.debug("Processing message from: " + emailMessage.getSentDate());
		File messageFile = new File(outputDir.getAbsolutePath()
				+ File.separator
				+ FILE_DATE_FORMAT.format(emailMessage.getSentDate()) + ".html");

		if (!messageFile.exists()) {
			log.debug("Creating message file");
			messageFile.createNewFile();
		}

		log.debug("Initializing templating context");
		VelocityContext context = new VelocityContext();
		context.put("emailMessage", emailMessage);

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
}
