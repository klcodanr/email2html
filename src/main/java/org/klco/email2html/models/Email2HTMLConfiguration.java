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
package org.klco.email2html.models;

// TODO: Auto-generated Javadoc
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
	 * The path to the folder containing the Velocity templates, must be set.
	 */
	private String templateDir;

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
	 * Gets the template dir.
	 *
	 * @return the template dir
	 */
	public String getTemplateDir() {
		return templateDir;
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
	 * @param folder
	 *            the new folder
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * Sets the output dir.
	 * 
	 * @param outputDir
	 *            the new output dir
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the search subject.
	 * 
	 * @param searchSubject
	 *            the new search subject
	 */
	public void setSearchSubject(String searchSubject) {
		this.searchSubject = searchSubject;
	}

	/**
	 * Sets the template dir.
	 *
	 * @param templateDir the new template dir
	 */
	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
