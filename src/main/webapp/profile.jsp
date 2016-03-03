<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.pitt.sis.exp.educvideos.entities.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Video Authoring</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- <link href="css/bootstrap.min.css" rel="stylesheet" media="screen"> -->
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<!-- jQuery library -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<!-- Latest compiled JavaScript -->
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<!-- JS file -->
	<script src="js/profile.js"></script>
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
					<li><a href="SettingsServlet">System Management</a></li> 
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
					<li class="active"><a href="ProfileServlet"><span class="glyphicon glyphicon-user"></span> Edit Profile</a></li>
					<li><a href="LoginServlet"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container-fluid">
		<div class="row">
		<div class="col-md-8 col-md-offset-2" style="text-align:center;">
			<h1>Manage Profile</h1>
		</div>
		</div>
		<%
			String username = "";
			String password = "";
			String email = "";
			try {
				User user = (User) request.getAttribute("user");
				if(user != null) {
					username = user.getUsername();
					password = user.getPassword();
					email = user.getEmail();
				}
			} catch (Exception e) {
				
			}
		%>
		<hr>
		<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<form action="javascript: saveUserInfo()" method="post" class="">
			<fieldset>
				<legend>Account Settings</legend>
				<div class="form-group">
				<label for="uname">Username:</label><input type="text" name="uname" placeholder="Username" required="required" class="form-control" id="username" readonly value="<%=username%>">
				</div>
				<div class="form-group">
				<label for="email">Email address:</label><input type="email" name="email" placeholder="Email address" required="required" class="form-control" id="email" value="<%=email%>">
				</div>
				<div class="form-group">
				<label for="password">Password:</label><input type="password" name="password" id="password" placeholder="Password" required="required" onblur="validatePassword()" class="form-control" value="<%=password%>">
				</div>
				<div class="form-group">
				<label for="confirm_password">Confirm password:</label><input type="password" name="confirm_password" id="confirm_password" placeholder="Confirm Password" required="required" onblur="validatePassword()" class="form-control" value="<%=password%>">
				</div>
				<div id="success-alert" class="alert alert-success fade-in" style="display: none;">
					<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
					<strong>Successfully updated!</strong>
				</div>
				<div id="error-alert" class="alert alert-danger fade-in" style="display: none;">
					<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
					<strong>Error!</strong> Something went wrong. Please try again later.
				</div>
				<input type="submit" value="Save" class="btn btn-info btn-lg"/>
			</fieldset>
			</form>
		</div>
		</div>
	</div>
</body>
</html>