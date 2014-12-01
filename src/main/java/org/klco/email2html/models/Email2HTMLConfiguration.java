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

import org.klco.email2html.Hook;

/**
 * Configures the Email -> HTML process.
 * 
 * @author dklco
 */
public class Email2HTMLConfiguration {

	/**
	 * Strings which break the original message and the replies.
	 */
	private String breakStrings = "<div class=\"gmail_quote,<hr,Sent from my,Technical details of permanent failure,Forwarded message,";

	/**
	 * Flag for excluding duplicate attachments and messages based on their MD5
	 * Checksum (if available).
	 */
	private boolean excludeDuplicates = true;

	/** The Java String format to generate the file names **/
	private String fileNameFormat = "%1$tY-%1$tm-%1$td-%2$s.html";

	/** The name of the folder to retrieve, defaults to 'Inbox'. */
	private String folder = "Inbox";

	/**
	 * The class for the hook to use
	 */
	private String hook;

	/**
	 * The actual hook object
	 */
	private Hook hookObj;

	/** The message template name. */
	private String template;

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
	 * The URL to connect to retrieve the email.
	 */
	private String url;

	/**
	 * The username with which to connect.
	 */
	private String username;

	/**
	 * The sub-directory under which images should be stored.
	 */
	private String imagesSubDir = "images/posts";

	/**
	 * The sub-directory under which the messages should be stored.
	 */
	private String messagesSubDir = "_posts";

	/**
	 * Gets the break strings.
	 * 
	 * @return the break strings
	 */
	public String getBreakStrings() {
		return breakStrings;
	}

	public String getFileNameFormat() {
		return fileNameFormat;
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
	 * @return the hook
	 */
	public String getHook() {
		return hook;
	}

	/**
	 * @return the hookObj
	 */
	public Hook getHookObj() {
		return hookObj;
	}

	/**
	 * @return the imagesSubDir
	 */
	public String getImagesSubDir() {
		return imagesSubDir;
	}

	/**
	 * @return the messagesSubDir
	 */
	public String getMessagesSubDir() {
		return messagesSubDir;
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
	 * @param excludeDuplicates
	 *            the new exclude duplicates
	 */
	public void setExcludeDuplicates(boolean excludeDuplicates) {
		this.excludeDuplicates = excludeDuplicates;
	}

	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
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
	 * @param hook
	 *            the hook to set
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void setHook(String hook) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		this.hook = hook;
		if (hook != null) {
			hookObj = (Hook) this.getClass().getClassLoader().loadClass(hook)
					.newInstance();
		}
	}

	/**
	 * @param imagesSubDir
	 *            the imagesSubDir to set
	 */
	public void setImagesSubDir(String imagesSubDir) {
		this.imagesSubDir = imagesSubDir;
	}

	/**
	 * @param messagesSubDir
	 *            the messagesSubDir to set
	 */
	public void setMessagesSubDir(String messagesSubDir) {
		this.messagesSubDir = messagesSubDir;
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
	 * Sets the overwrite flag.
	 * 
	 * @param overwrite
	 *            the new overwrite flag
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
	 * @param renditions
	 *            the new renditions
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
	 * Sets the template.
	 * 
	 * @param template
	 *            the template
	 */
	public void setTemplate(String template) {
		this.template = template;
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
