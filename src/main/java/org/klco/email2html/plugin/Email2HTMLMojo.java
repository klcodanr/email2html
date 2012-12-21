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
	private String breakStrings = "<div class=\"gmail_quote,<hr,Sent from my iPhone,Sent from my iPad,Sent from my mobile,Technical details of permanent failure,Forwarded message";

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
	 * The path to the folder containing the templates.
	 * 
	 * @parameter
	 * @required
	 */
	private String templateDir;

	/**
	 * The height to create thumbnails of any image attachments, set as -1 to
	 * not create thumbnails.
	 * 
	 * @parameter default-value="100"
	 */
	private int thumbnailHeight;

	/**
	 * The URL to connect to retrieve the email, must be set.
	 * 
	 * @parameter
	 * @required
	 */
	private String url;

	/**
	 * The width to create thumbnails of any image attachments, set as -1 to not
	 * create thumbnails.
	 * 
	 * @parameter default-value="100"
	 */
	private int thumbnailWidth;

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
		config.setFolder(folder);
		config.setIndexTemplateNames(indexTemplateNames);
		config.setMessageTemplateName(messageTemplateName);
		config.setOutputDir(outputDir);
		config.setOverwrite(String.valueOf(overwrite));
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
		config.setSearchSubject(searchSubject);
		config.setTemplateDir(templateDir);
		config.setThumbnailHeight(String.valueOf(thumbnailHeight));
		config.setThumbnailWidth(String.valueOf(thumbnailWidth));
		config.setUrl(url);
		config.setUsername(server.getUsername());

		getLog().info("Starting reader");
		EmailReader reader = new EmailReader(config);
		reader.readEmails();

		getLog().info("Reader complete");
	}
}