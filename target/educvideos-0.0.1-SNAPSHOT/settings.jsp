<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.util.List" %>
<%@ page import="edu.pitt.sis.exp.educvideos.videoauthoring.models.DomainModel" %>
<%@ page import="edu.pitt.sis.exp.educvideos.videoauthoring.models.TopicModel" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Video Authoring</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<!-- Adding the corresponding settings javascript file -->
<script src="js/settings.js"></script>
</head>
<body>
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Video Authoring</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li><a href="search.jsp">Search</a></li>
					<li><a href="BookmarksServlet">Bookmarks</a></li>
					<li><a href="LibraryServlet">Library</a></li>
					<li class="active"><a href="SettingsServlet">System Management</a></li> 
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#">Welcome <% 
					try {
						if(session!=null) {
							String user = (String) session.getAttribute("user");
							if(user == null) {
								session.invalidate();
								request.setAttribute("error", "Session ended. You have to login again.");
								ServletContext servletContext = getServletContext();
								response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
							}
							out.println(user);
						} else {
							request.setAttribute("error", "Session ended. You have to login again.");
							ServletContext servletContext = getServletContext();
							response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
						}
					} catch(Exception e) {} %></a></li>
					<li><a href="ProfileServlet"><span class="glyphicon glyphicon-user"></span> Edit Profile</a></li>
					<li><a href="LoginServlet"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="row">
			<div class="col-md-8 col-md-offset-2" style="text-align:center;">
				<h1>System Management</h1>
			</div>
		</div>
		<hr>
		<div id="settings-container" class="panel-group col-md-10 col-md-offset-1">
		<%
			try {
				List<DomainModel> domainModels = (List<DomainModel>) request.getAttribute("domainModels");
				if(domainModels!=null) {
					for(DomainModel domainModel: domainModels) {
						out.println("<div class=\"panel panel-default\">");
						out.println("<div class=\"panel-heading\"><h4 class=\"panel-title\">");
						out.println(domainModel.getTitle());
						out.println("<span class=\"pull-right\">");
						out.println("<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"viewDomain("+domainModel.getId()+")\" data-toggle=\"modal\" data-target=\"#domainModal\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>");
						out.println("<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"editDomain("+domainModel.getId()+")\" data-toggle=\"modal\" data-target=\"#domainModal\"><span class=\"glyphicon glyphicon-pencil\"></span></button>");
						out.println("<button type=\"button\" class=\"btn btn-primary btn-xs\"><span class=\"glyphicon glyphicon-trash\"></span></button>");
						out.println("</span>");
						out.println("</h4></div>");
						out.println("<div class=\"panel-body\">");
						out.println("<ul class=\"list-group\">");
						for(TopicModel topicModel: domainModel.getTopics()) {
							out.println("<li class=\"list-group-item\">");
							out.println(topicModel.getTitle());
							out.println("<span class=\"pull-right\">");
							out.println("<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"viewTopic("+topicModel.getId()+")\" data-toggle=\"modal\" data-target=\"#topicModal\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>");
							out.println("<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"editTopic("+topicModel.getId()+")\" data-toggle=\"modal\" data-target=\"#topicModal\"><span class=\"glyphicon glyphicon-pencil\"></span></button>");
							out.println("<button type=\"button\" class=\"btn btn-primary btn-xs\"><span class=\"glyphicon glyphicon-trash\"></span></button>");
							out.println("</span>");
							out.println("</li>");
						}
						//HTML code for adding new topic
						out.println("<li class=\"list-group-item\">");
						out.println("<button type=\"button\" class=\"btn btn-primary\" onclick=\"populateAddTopicModal("+domainModel.getId()+",'"+domainModel.getTitle()+"')\" data-toggle=\"modal\" data-target=\"#topicModal\">Add new topic in "+domainModel.getTitle()+"</button>");
						out.println("</li>");
						out.println("</ul>");
						out.println("</div></div>");
					}
					String user = (String) session.getAttribute("user");
					String currentTime = Calendar.getInstance().getTime().toString();
					out.println("<div class=\"panel panel-default\">");
					out.println("<div class=\"panel-heading\"><h4 class=\"panel-title\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"populateAddDomainModal('"+user+"','"+currentTime+"')\" data-toggle=\"modal\" data-target=\"#domainModal\">Add new domain</button></h4></div>");
					out.println("</div>");
				}
			} catch(Exception e) { }
		%>
		</div>
	</div>
	
	<!-- Domain Modal -->
	<div class="modal fade" id="domainModal" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="domain-modal-title">Add Domain</h4>
				</div>
				<div class="modal-body" id="modal-body">
					<form id="domainForm">
						<input id="domain_id" type="hidden">
						<div class="form-group">
							<label for="domain_name">Name:</label><input id="domain_name" type="text" name="domain_name" placeholder="Name of Domain" required="required" class="form-control" readonly="">
						</div>
						<div class="form-group">
							<label for="domain_title">Title:</label><input id="domain_title" type="text" name="domain_title" placeholder="Title of Domain" required="required" class="form-control" readonly="">
						</div>
						<div class="form-group">
							<label for="domain_description">Description:</label><input id="domain_description" type="text" name="domain_description" placeholder="Description of Domain" required="required" class="form-control" readonly="">
						</div>
						<div class="form-group">
							<label for="domain_author">Author:</label><input id="domain_author" type="text" name="domain_author" value="
							<%
							try {
									String user = (String) session.getAttribute("user");
									out.println(user);
							} catch(Exception e) {}
							%>" readonly class="form-control" readonly="">
						</div>
						<div class="form-group">
							<label for="domain_license">License:</label><input id="domain_license" type="text" name="domain_license" placeholder="License of Domain" required="required" class="form-control" readonly="">
						</div>
						<div class="form-group">
							<label for="domain_version">Version:</label><input id="domain_version" type="text" name="domain_version" placeholder="Version of Domain" required="required" class="form-control" readonly="">
						</div>
						<div class="form-group">
							<label for="domain_created">Created on:</label><input id="domain_created" type="text" name="domain_created" readonly required="required" class="form-control" readonly="">
						</div>
						<div class="form-group">
							<label for="domain_modified">Last modified on:</label><input id="domain_modified" type="text" name="domain_modified" readonly required="required" class="form-control" readonly="">
						</div>
					</form>
				</div>
				<div class="modal-footer" id="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary" id="domain-modal-submit" onclick="saveDomain()">Submit</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Topic Modal -->
	<div class="modal fade" id="topicModal" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="topic-modal-title">Add Topic</h4>
				</div>
				<div class="modal-body" id="modal-body">
					<input id="topic_id" type="hidden">
					<input id="topic_domainId" type="hidden">
					<div class="form-group">
						<label for="topic_title">Title:</label><input id="topic_title" type="text" name="topic_title" placeholder="Title of Topic" required="required" class="form-control" readonly="">
					</div>
					<div class="form-group">
						<label for="topic_description">Description:</label><input id="topic_description" type="text" name="topic_description" placeholder="Description of Topic" required="required" class="form-control" readonly="">
					</div>
				</div>
				<div class="modal-footer" id="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary" id="topic-modal-submit" onclick="saveTopic()">Submit</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>