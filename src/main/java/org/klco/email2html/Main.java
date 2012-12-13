package org.klco.email2html;
import java.util.Properties;

import org.klco.email2html.EmailReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.trace("main");

		if (args.length != 0) {
			Properties props = new Properties();
			try {
				props.load(Main.class.getClassLoader().getResourceAsStream(
						"email-reader.properties"));
				EmailReader reader = new EmailReader(props);
				reader.readEmails(args[0]);
			} catch (Exception e) {
				log.error("Unexpected exception loading emails", e);
			}
		} else {
			log.warn("Password is required!");
		}

	}

}
