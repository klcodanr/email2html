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
package org.klco.email2html.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;

import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.models.Rendition;
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
	 * @param props
	 *            the props
	 * @return the email2 html configuration
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	public static Email2HTMLConfiguration loadProperties(Properties props) throws InstantiationException, ClassNotFoundException {
		log.trace("loadProperties");
		Email2HTMLConfiguration config = new Email2HTMLConfiguration();

		for (Object key : props.keySet()) {
			Field field = null;
			try {
				field = config.getClass().getDeclaredField(key.toString());
				String value = props.getProperty(key.toString());
				if (field.getName().equals("renditions")) {
					String[] renditionStrs = value.split("\\,");
					Rendition[] renditions = new Rendition[renditionStrs.length];
					for (int i = 0; i < renditionStrs.length; i++) {
						renditions[i] = Rendition.parse(renditionStrs[i]);
					}
					log.info("Setting property {} to value {}", key, Arrays.toString(renditions));
					field.setAccessible(true);
					field.set(config, renditions);
				}else if(field.getName().equals("overwrite") || field.getName().equals("excludeDuplicates")){
					Boolean overwrite = Boolean.valueOf(value); 
					log.info("Setting property {} to value {}", key, overwrite);
					field.setAccessible(true);
					field.set(config, overwrite);
				} else if(field.getName().equals("hook")){
					config.setHook(value);
				} else {
					log.info("Setting property {} to value {}", key, value);
					field.setAccessible(true);
					field.set(config, value);
				}

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
