Email2HTML
==========

Simple library and maven plugin for converting emails into a HTML site.

Email2HTML retrieves messages from an IMAP mail server and attempts to extract just the useful information by trimming replies, getting sender names and getting/sanitizing HTML content from the email.

It saves email attachments to the filesystem and uses a templating engine to write the message to a file and allows for creating multiple index files.

Use
----------

Email2HTML is currently not available in the Maven Central Repository.  The only way to get it is to download the code and install it with the command:

	mvn clean install

Email2HTML can be invoked either as part of a maven build or through the command line.

To invoke through the command line, use the entry point: org.klco.email2html.Main.  To configure the application through the command line, create a file called email2html.properties and load it into the classpath.

The application takes the following configuration values:

* **breakStrings** - Strings which break the original message and the replies.
* **folder** - The name of the folder to retrieve, defaults to 'Inbox'.
* **excludeDuplicates**  - Flag for excluding duplicate attachments based on their MD5 Checksum (if available)
* **indexTemplateNames** - A comma-separated list of index templates, see templating below for more.
* **messageTemplateName** - The message template name, see templating below for more.
* **outputDir** - The output directory to which to save the files.
* **overwrite** - Whether or not to overwrite existing content.
* **password** - The password to use to connect to the mail server, must be set.
* **searchSubject** - The subject of the emails to search for, optional.
* **templateDir** - The path to the folder containing the Velocity templates, must be set.
* **renditions** - The renditions to create, in the format: [name1] [height1] [width1] [fill1 (optional)], [name2] [height2] [width2] [fill2 (optional)]
* **url** - The URL to connect to retrieve the email.
* **username** - The username with which to connect to the mail server.

The second option is to add Email2HTML as a plugin in a Maven build.  To do this add a plugin to your POM similar to the following:

	<plugins>
		<plugin>
			<groupId>org.klco.email2html</groupId>
			<artifactId>email2html</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<executions>
				<execution>
					<id>download-code</id>
					<phase>verify</phase>
					<goals>
						<goal>extractEmails</goal>
					</goals>
				</execution>
			</executions>
			<configuration>
				<templateDir>${basedir}/src/main/resources</templateDir>
				<folder>Inbox</folder>
				<url>imap.gmail.com</url>
				<serverId>gmail</serverId>
				<outputDir>${basedir}/target/site</outputDir>
				<indexTemplateNames>index.html.vm,all.html.vm</indexTemplateNames>
				<renditions>
					<rendition>
						<name>thumbnail</name>
						<width>100</width>
						<height>100</height>
						<fill>true</fill>
					</rendition>
					<rendition>
						<name>web</name>
						<width>800</width>
						<height>800</height>
					</rendition>
				</renditions>
			</configuration>
		</plugin>

The Email2HTML Maven plugin takes the following configuration values:

* **breakStrings** - Strings which break the original message and the replies.
* **excludeDuplicates**  - Flag for excluding duplicate attachments based on their MD5 Checksum (if available)
* **folder** - The name of the folder to retrieve, defaults to 'Inbox'.
* **indexTemplateNames** - A comma-separated list of index templates, see templating below for more.
* **messageTemplateName** - The message template name, see templating below for more.
* **outputDir** - The output directory to which to save the files.
* **overwrite** - Whether or not to overwrite existing content.
* **renditions** - The renditions to create, with the keys: name, width, height and optionally, fill.
* **searchSubject** - The subject of the emails to search for, optional.
* **serverId** - The id of the server to retrieve from the settings.xml, must contain the username and password.
* **templateDir** - The path to the folder containing the Velocity templates, must be set.
* **url** - The URL to connect to retrieve the email.

Templates 
----------

Email2HTML uses [Apache Velocity](http://velocity.apache.org/).  All templates will be passed the Velocity general tools.  The names of the files and image folders can be derived from the sentDate in the format *yyyy-MM-dd-HH-mm-ss*.  Two different types of templates are available:

**Message Template**

Template for rendering a single message.  This will be passed an instance of org.klco.email2html.models.EmailMessage with the data loaded from the server in the emailMessage variable.  A file will then be created based on the date the email was sent.

**Index Templates**

You can specify multiple index templates.  You can specify multiple templates each template will result in one page being creating with the name of the template before '.vm'.  These templates will be passed a sorted list of EmailMessages in the messages variable.

License 
---------- 

Copyright (C) 2012  Dan Klco

**MIT License**

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
