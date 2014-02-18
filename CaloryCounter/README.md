CaloryCounter
=============

System for recording of sport activities and burnt calories counter. It comprises of a full server-side web application, and a separate AJAX-based RESTful client-side web application with limited functionality (only user management and activity records management).

To run the project, proceed with following steps in order:

0. connect to the database
1. clean & install whole project, if there is a target directory or any .class files (from the parent module, run mvn clean install), and/or if you miss some dependencies in your local maven repository.
2. in the CaloryCounter-Web module (directory), run "mvn tomcat:run", this will deploy the application to an embedded Tomcat server. 
3. in the CaloryCounter-REST-Jersey-Client module (directory), run "mvn -Dmaven.tomcat.port=8181 tomcat:run-war". This will deploy the RESTful Client Web application to an embedded Tomcat server. In your web browser, use the address shown in the tomcat log (defaultly, it should be localhost:8181/CaloryCounter-REST-Jersey-Client). If you run Tomcat 7.X, the default address should be localhost:8181/pa165.
4. If you want to use the full web application on server side of the project, use the same address, but with port 8080 (or any port defaultly configured with your maven tomcat plugin) instead of 8181.

Enjoy!
