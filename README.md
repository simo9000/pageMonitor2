=================READ ME - PageMonitor Back End====================

The back end code base is built as a MAVEN project and will need to be installed as such. The project is a mixture of java and scala so the initial install may be lengthy as MAVEN installs 
Scala files. NOTE: the front end and back end of the page monitor application will need be located on the same machine. It is recommend that the back end be installed first. Installing this
application requires java and maven to be installed and the path variables be set correctly.

Install steps

1. start cmd and navigate to the pageMonitor2 directory from the git clone download

2. execute 'mvn install' <- may take a while depending on network connection

3. execute 'mvn exec:java'

Once running the debug messages will print to the command prompt. Leave the window open and proceed to front end installation. 
Front end located @ https://bitbucket.org/Ironwalnut/pagemonitor-web-application/overview