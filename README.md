# EducVideos - Video Authoring Application

This application helps authors to search for educational videos such as tutorials and examples from YouTube. Videos can be bookmarked for personal use as well as added to the library which can be viewed by students for an additional understanding.

## How to use this repository
### Softwares/tools used
* JDK6
* Eclipse IDE (or any other IDE)
* Apache Tomcat 6
* Apache Maven (Eclipse Luna and newer versions come with M2Eclipse in them)
* MySQL 5.5

### Setup
* Pull the git repository. Using CLI: 
`$ git clone https://github.com/PAWSLabUniversityOfPittsburgh/educvideos.git`
* Add Apache Tomcat 6 server in Eclipse.
* In Eclipse: Import > Existing Projects into Workspace > Select root directory of cloned 'educvideos' repository > Click 'Finish'.
* Right click on pom.xml > Run As > Maven build > Type `clean install` in Goals. Check the boxes for 'Update Snapshots', 'Debug Output', 'Skip Tests' > Click 'Run'. All the required jars will be downloaded.
* Update the URL and credentials for MySQL database in `educvideos/src/main/resources/hibernate.cfg.xml`.
* Make sure the MySQL server is running.
* Add 'educvideos' on Tomcat server and Start the server.   
On the browser, hit the URL `http://localhost:{Tomcat_port_number}/educvideos`.

### Architecture
This application contains 3 different parts:
* **Video Authoring** - Video Authoring uses Servlets + JSP, and Hibernate. It helps the authors create video segments using the YouTube Search API.
* **Video Tracking** - Video Tracking uses RESTful API + JavaScript, and Hibernate. It tracks the portion of video segments watched by a user. This data is saved in local MySql database, as well as sent to the Aggregator for User Modelling, through AJAX requests.
* **Content Brokering** - Content Brokering uses RESTful API and Hibernate. It is a web service that offers the details of the latest video content created by authors using the Video Authoring tool, in order to be registered for User Modelling.
