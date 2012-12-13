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

import java.io.File;
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
	 * Sets the subject.
	 * 
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
