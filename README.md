Email2HTML
==========

Simple library for converting emails into a HTML site.

Email2HTML retrieves messages from an IMAP mail server and attempts to extract just the useful information by trimming replies, getting sender names and getting/sanitizing HTML content from the email.

It saves email attachments to the filesystem and uses a templating engine to write the message to a file and allows for creating multiple index files.

Use
----------

Email2HTML is currently not available in the Maven Central Repository.  The only way to get it is to download the code and install it with the command:

	mvn clean install

To invoke Email2HTML, execute the jar as such:

    java -jar email2html-0.0.2-SNAPSHOT-jar-with-dependencies.jar config.properties

The config.properties is a file which contains the configuration values for running Email2HTML.  The available configuration values are:

* **breakStrings** - Strings which break the original message and the replies.
* **excludeDuplicates**  - Flag for excluding duplicate attachments based on a CRC32 Checksum
* **fileNameFormat** - The Java String format to generate the file names, defaults to %1$tY-%1$tm-%1$td-%2$s.html
* **folder** - The name of the folder to retrieve, defaults to 'Inbox'.
* **hook** - The fully qualified class name of a class which implements the Hook interface and is available in the classpath.  Used for extending the functionality of Email2HTML.
* **imagesSubDir** - The sub-directory under which images should be stored.
* **messagesSubDir** - The sub-directory under which the messages should be stored.
* **outputDir** - The output directory to which to save the files.
* **overwrite** - Whether or not to overwrite existing content.
* **password** - The password to use to connect to the mail server, must be set.
* **renditions** - The renditions to create, in the format: [name1] [height1] [width1] [fill1 (optional)], [name2] [height2] [width2] [fill2 (optional)]
* **searchSubject** - The subject of the emails to search for, optional.
* **template** - The message template name, see templating below for more.
* **url** - The URL to connect to retrieve the email.
* **username** - The username with which to connect to the mail server.

License 
---------- 

Copyright (C) 2012  Dan Klco

**MIT License**

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
