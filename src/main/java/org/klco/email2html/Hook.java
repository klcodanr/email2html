package org.klco.email2html;

import java.io.File;
import java.util.Map;

import javax.mail.Message;

import org.klco.email2html.models.Email2HTMLConfiguration;
import org.klco.email2html.models.EmailMessage;

/**
 * An interface for representing hook classes which can be specified and used to
 * modify the behavior of the application.
 * 
 * @author dklco
 */
public interface Hook {

	/**
	 * Gets executed after the entire process is complete.
	 */
	public void afterComplete();

	/**
	 * Will be executed after the EmailMessage is read from the email server.
	 * 
	 * @param sourceMessage
	 *            the message from the server
	 * @param message
	 *            the message created from the source message
	 */
	public void afterRead(Message sourceMessage, EmailMessage message);

	/**
	 * Will be executed after the message is written to the filesystem.
	 * 
	 * @param message
	 *            the message read from the server
	 * @param params
	 *            the parameters used to create the file
	 * @param file
	 *            the file
	 */
	public void afterWrite(EmailMessage message, Map<String, Object> params,
			File file);

	/**
	 * Will be executed before the message is written to the filesystem.
	 * 
	 * @param message
	 *            the message read from the server
	 * @param params
	 *            the parameters used to create the file
	 * @param file
	 *            the file
	 */
	public void beforeWrite(EmailMessage message, Map<String, Object> params);

	/**
	 * Initialize the hook, this will be run before processing begins.
	 * 
	 * @param config
	 *            the configuration
	 */
	public void init(Email2HTMLConfiguration config);
}
