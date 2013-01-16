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

import org.klco.email2html.plugin.Rendition;

// TODO: Auto-generated Javadoc
/**
 * Configures the Email -> HTML process.
 * 
 * @author dklco
 */
public class Email2HTMLConfiguration {

	/**
	 * Strings which break the original message and the replies.
	 */
	private String breakStrings;
	
	/** Flag for excluding duplicate attachments based on their MD5 Checksum (if available). */
	private boolean excludeDuplicates = true;

	/** The name of the folder to retrieve, defaults to 'Inbox'. */
	private String folder = "Inbox";

	/** The index template names. */
	private String indexTemplateNames;

	/** The message template name. */
	private String messageTemplateName;

	/**
	 * The output directory to which to save the files, defaults to the current
	 * directory.
	 */
	private String outputDir;

	/**
	 * Whether or not to overwrite existing content.
	 */
	private boolean overwrite;

	/**
	 * The password to use to connect, must be set.
	 */
	private String password;

	/**
	 * The image renditons to be created.
	 */
	private Rendition[] renditions;

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
	 * Gets the break strings.
	 * 
	 * @return the break strings
	 */
	public String getBreakStrings() {
		return breakStrings;
	}

	/**
	 * Get the folder.
	 * 
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Gets the index template names.
	 * 
	 * @return the index template names
	 */
	public String getIndexTemplateNames() {
		return indexTemplateNames;
	}

	/**
	 * Gets the message template name.
	 * 
	 * @return the message template name
	 */
	public String getMessageTemplateName() {
		return messageTemplateName;
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
	 * Gets the overwrite.
	 * 
	 * @return the overwrite
	 */
	public boolean getOverwrite() {
		return overwrite;
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
	 * Gets the renditions.
	 *
	 * @return the renditions
	 */
	public Rendition[] getRenditions() {
		return renditions;
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
	 * Checks if is exclude duplicates.
	 *
	 * @return true, if is exclude duplicates
	 */
	public boolean isExcludeDuplicates() {
		return excludeDuplicates;
	}

	/**
	 * Sets the break strings.
	 * 
	 * @param breakStrings
	 *            the new break strings
	 */
	public void setBreakStrings(String breakStrings) {
		this.breakStrings = breakStrings;
	}

	/**
	 * Sets the exclude duplicates.
	 *
	 * @param excludeDuplicates the new exclude duplicates
	 */
	public void setExcludeDuplicates(boolean excludeDuplicates) {
		this.excludeDuplicates = excludeDuplicates;
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
	 * Sets the index template names.
	 * 
	 * @param indexTemplateNames
	 *            the new index template names
	 */
	public void setIndexTemplateNames(String indexTemplateNames) {
		this.indexTemplateNames = indexTemplateNames;
	}

	/**
	 * Sets the message template name.
	 * 
	 * @param messageTemplateName
	 *            the new message template name
	 */
	public void setMessageTemplateName(String messageTemplateName) {
		this.messageTemplateName = messageTemplateName;
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
	 * Sets the overwrite.
	 * 
	 * @param overwrite
	 *            the new overwrite
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
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
	 * Sets the renditions.
	 *
	 * @param renditions the new renditions
	 */
	public void setRenditions(Rendition[] renditions) {
		this.renditions = renditions;
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
	 * @param templateDir
	 *            the new template dir
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
