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
package org.klco.email2html.models;

/**
 * Configures the Email -> HTML process.
 * 
 * @author dklco
 */
public class Email2HTMLConfiguration {
	
	/** The name of the folder to retrieve, defaults to 'Inbox'. */
	private String folder = "Inbox";

	/**
	 * The output directory to which to save the files, defaults to the current
	 * directory.
	 */
	private String outputDir;

	/**
	 * The password to use to connect, must be set.
	 */
	private String password;

	/**
	 * The subject of the emails to search for, optional.
	 */
	private String searchSubject;

	/**
	 * The path to the HTML Template to use, must be set.
	 */
	private String template;

	/**
	 * The URL to connect to retrieve the email.
	 */
	private String url;

	/**
	 * The username with which to connect.
	 */
	private String username;

	/**
	 * Get the folder.
	 * 
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Gets the output dir.
	 *
	 * @return the output dir
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the search subject.
	 *
	 * @return the search subject
	 */
	public String getSearchSubject() {
		return searchSubject;
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the folder.
	 *
	 * @param folder the new folder
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * Sets the output dir.
	 *
	 * @param outputDir the new output dir
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the search subject.
	 *
	 * @param searchSubject the new search subject
	 */
	public void setSearchSubject(String searchSubject) {
		this.searchSubject = searchSubject;
	}

	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
