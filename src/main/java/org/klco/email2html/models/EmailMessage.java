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

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * The Class EmailMessage.
 * 
 * @author dklco
 */
public class EmailMessage {

	/** The attachments. */
	private List<File> attachments;

	/** The full message. */
	private String fullMessage;

	/** The message. */
	private String message;

	/** The sender. */
	private String sender;

	/** The sent date. */
	private Date sentDate;

	/** The subject. */
	private String subject;

	/**
	 * Gets the attachments.
	 * 
	 * @return the attachments
	 */
	public List<File> getAttachments() {
		return attachments;
	}

	/**
	 * Gets the full message.
	 * 
	 * @return the full message
	 */
	public String getFullMessage() {
		return fullMessage;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Gets the sent date.
	 * 
	 * @return the sent date
	 */
	public Date getSentDate() {
		return sentDate;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the attachments.
	 * 
	 * @param attachments
	 *            the new attachments
	 */
	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	/**
	 * Sets the full message.
	 * 
	 * @param fullMessage
	 *            the new full message
	 */
	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param sender
	 *            the new sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Sets the sent date.
	 * 
	 * @param sentDate
	 *            the new sent date
	 */
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * Sets the subject.
	 * 
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
