/*
 * This file is part of Email2HTML.
 * 
 * Email2HTML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Email2HTML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Email2HTML.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.klco.email2html.utils;

import java.lang.reflect.Field;
import java.util.Properties;

import org.klco.email2html.models.Email2HTMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilizes for managing the application configuration.
 * 
 * @author dklco
 */
public class ConfigurationUtils {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory
			.getLogger(ConfigurationUtils.class);

	/**
	 * Load properties.
	 *
	 * @param props the props
	 * @return the email2 html configuration
	 */
	public static Email2HTMLConfiguration loadProperties(Properties props) {
		log.trace("loadProperties");
		Email2HTMLConfiguration config = new Email2HTMLConfiguration();

		for (Object key : props.keySet()) {
			Field field = null;
			try {
				field = config.getClass().getDeclaredField(key.toString());

				String value = props.getProperty(key.toString());
				log.info("Setting property {} to value {}", key, value);
				field.setAccessible(true);
				field.set(config, value);
			} catch (SecurityException e) {
				log.warn("Exception getting field for property " + key, e);
			} catch (NoSuchFieldException e) {
				log.warn("No field found for property " + key, e);
			} catch (IllegalArgumentException e) {
				log.warn("Invalid value when setting property " + key, e);
			} catch (IllegalAccessException e) {
				log.warn("Exception setting value for property " + key, e);
			}
		}
		return config;
	}
}
