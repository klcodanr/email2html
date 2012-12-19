package org.klco.email2html.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.klco.email2html.EmailReader;
import org.klco.email2html.models.Email2HTMLConfiguration;

/**
 * Extracts the emails from the email server and writes the resulting pages to
 * HTML files.
 * 
 * @author dklco
 * @goal extractEmails
 * @phase site
 */
public class Email2HTMLMojo extends AbstractMojo {

	/**
	 * The name of the folder to retrieve, defaults to 'Inbox'.
	 * 
	 * @parameter default-value="Inbox"
	 */
	private String folder = "Inbox";

	/**
	 * The path to the folder containing the templates.
	 * 
	 * @parameter
	 * @required
	 */
	private String templateDir;

	/**
	 * The name of the template to use for messages.
	 * 
	 * @parameter default-value="message.vm"
	 */
	private String messageTemplateName;

	/**
	 * The name(s) of the template(s) to create as 'index' files. These files
	 * will be passed all of the loaded messages. Comma separate the values.
	 * 
	 * @parameter default-value="index.html.vm"
	 */
	private String indexTemplateNames;

	/**
	 * The output directory to which to save the files, defaults to the current
	 * directory.
	 * 
	 * @parameter default-value="${basedir}/target"
	 */
	private String outputDir;

	/**
	 * The password to use to connect, must be set.
	 * 
	 * @parameter
	 * @required
	 */
	private String password;

	/**
	 * The subject of the emails to search for, optional.
	 * 
	 * @parameter
	 */
	private String searchSubject;

	/**
	 * The URL to connect to retrieve the email, must be set.
	 * 
	 * @parameter
	 * @required
	 */
	private String url;

	/**
	 * The username with which to connect, must be set.
	 * 
	 * @parameter
	 * @required
	 */
	private String username;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		getLog().info("Execute");

		Email2HTMLConfiguration config = new Email2HTMLConfiguration();
		config.setFolder(folder);
		config.setIndexTemplateNames(indexTemplateNames);
		config.setMessageTemplateName(messageTemplateName);
		config.setOutputDir(outputDir);
		config.setPassword(password);
		config.setSearchSubject(searchSubject);
		config.setTemplateDir(templateDir);
		config.setUrl(url);
		config.setUsername(username);

		getLog().info("Starting reader");
		EmailReader reader = new EmailReader(config);
		reader.readEmails();

		getLog().info("Reader complete");
	}
}