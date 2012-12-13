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

import java.util.Properties;

import org.klco.email2html.EmailReader;
import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry point, can be used to invoke the library from the command
 * line.
 * 
 * @author dklco
 */
public class Main {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	/** The Constant PROPERTIES_FILE_NAME. */
	public static final String PROPERTIES_FILE_NAME = "email2html.properties";

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		log.trace("main");

		Properties props = new Properties();
		try {
			props.load(Main.class.getClassLoader().getResourceAsStream(
					PROPERTIES_FILE_NAME));
			Email2HTMLConfiguration config = ConfigurationUtils
					.loadProperties(props);
			EmailReader reader = new EmailReader(config);
			reader.readEmails();
		} catch (Exception e) {
			log.error("Unexpected exception loading emails", e);
		}

	}
}
