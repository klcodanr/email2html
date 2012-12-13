package org.klco.email2html;

import java.util.Properties;

import org.klco.email2html.EmailReader;
import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.utils.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	public static final String PROPERTIES_FILE_NAME = "email2html.properties";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.trace("main");

		if (args.length != 0) {
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
		} else {
			log.warn("Password is required!");
		}

	}
}
