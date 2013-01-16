package org.klco.email2html.plugin;

import java.util.ArrayList;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.klco.email2html.EmailReader;
import org.klco.email2html.models.Email2HTMLConfiguration;
import org.slf4j.impl.SimpleLogger;

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
	 * Comma separated list of strings which break content from replies.
	 * 
	 * @parameter
	 */
	private String breakStrings = "<div class=\"gmail_quote,<hr,Sent from my,Technical details of permanent failure,Forwarded message,";


	/** 
	 * Flag for excluding duplicate attachments based on their MD5 Checksum (if available).
	 * 
	 * @parameter default-value="true"
	 */
	private boolean excludeDuplicates = true;
	
	/**
	 * The name of the folder to retrieve, defaults to 'Inbox'.
	 * 
	 * @parameter default-value="Inbox"
	 */
	private String folder = "Inbox";

	/**
	 * The name(s) of the template(s) to create as 'index' files. These files
	 * will be passed all of the loaded messages. Comma separate the values.
	 * 
	 * @parameter default-value="index.html.vm"
	 */
	private String indexTemplateNames;

	/**
	 * The name of the template to use for messages.
	 * 
	 * @parameter default-value="message.vm"
	 */
	private String messageTemplateName;

	/**
	 * The output directory to which to save the files, defaults to the current
	 * directory.
	 * 
	 * @parameter default-value="${basedir}/target"
	 */
	private String outputDir;

	/**
	 * Whether or not to overwrite the existing content.
	 * 
	 * @parameter default-value="false"
	 */
	private boolean overwrite;

	/**
	 * The rendition images to create, should be in the format
	 * 
	 * @parameter
	 * @required
	 */
	private Rendition[] renditions;

	/**
	 * The subject of the emails to search for, optional.
	 * 
	 * @parameter
	 */
	private String searchSubject;

	/**
	 * The id of the server to retrieve from the settings.xml and use.
	 * 
	 * @parameter
	 * @required
	 */
	private String serverId;

	/**
	 * @parameter default-value="${settings}"
	 */
	private Settings settings;

	/**
	 * Allows me to decrypt passwords.
	 * 
	 * @component role= "org.apache.maven.settings.crypto.SettingsDecrypter"
	 * @required
	 */
	private SettingsDecrypter settingsDecrypter;

	/**
	 * The path to the folder containing the templates.
	 * 
	 * @parameter
	 * @required
	 */
	private String templateDir;

	/**
	 * The URL to connect to retrieve the email, must be set.
	 * 
	 * @parameter
	 * @required
	 */
	private String url;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		getLog().info("Execute");

		final Server server = settings.getServer(serverId);

		Email2HTMLConfiguration config = new Email2HTMLConfiguration();
		config.setBreakStrings(breakStrings);
		config.setExcludeDuplicates(excludeDuplicates);
		config.setFolder(folder);
		config.setIndexTemplateNames(indexTemplateNames);
		config.setMessageTemplateName(messageTemplateName);
		config.setOutputDir(outputDir);
		config.setOverwrite(overwrite);
		SettingsDecryptionRequest request = new DefaultSettingsDecryptionRequest();
		request.setServers(new ArrayList<Server>() {
			private static final long serialVersionUID = 1L;
			{
				add(server);
			}
		});
		String password = settingsDecrypter.decrypt(request).getServer()
				.getPassword();
		config.setPassword(password);
		config.setRenditions(renditions);
		config.setSearchSubject(searchSubject);
		config.setTemplateDir(templateDir);
		config.setUrl(url);
		config.setUsername(server.getUsername());

		getLog().info("Starting reader");
		EmailReader reader = new EmailReader(config);
		reader.readEmails();

		getLog().info("Reader complete");
	}
}
