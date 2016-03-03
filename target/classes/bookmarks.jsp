<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="edu.pitt.sis.exp.educvideos.videoauthoring.models.BookmarkModel" %>
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
<script src="js/bookmarks.js"></script>
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
					<li class="active"><a href="BookmarksServlet">Bookmarks</a></li>
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
					<li><a href="ProfileServlet"><span class="glyphicon glyphicon-user"></span> Edit Profile</a></li>
					<li><a href="LoginServlet"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="row">
			<div class="col-md-8 col-md-offset-2" style="text-align:center;">
				<h1>Bookmarks</h1>
			</div>
		</div>
		<hr>
		<%
			List<BookmarkModel> bookmarkModels = (List<BookmarkModel>) request.getAttribute("bookmarkModels");
			
			if(bookmarkModels!=null) {
				out.println("");
				for(BookmarkModel bookmarkModel: bookmarkModels) {
					out.println("<div class=\"panel panel-default\">");
					out.println("<div class=\"panel-body\">");
					out.println("<div class=\"row\">");
					out.println("<div class=\"col-md-3\"><img src=\"http://img.youtube.com/vi/"+bookmarkModel.getYoutubeId()+"/default.jpg\" class=\"img-thumbnail\" width=\"152\" height=\"118\" /></div>");
					out.println("<div class=\"col-md-6\">");
					out.println("<ul class=\"list-unstyled\">");
					out.println("<li class=\"h4\">"+bookmarkModel.getVideoTitle()+"</li>");
					out.println("<li>"+bookmarkModel.getQuery()+"</li>");
					out.println("<li class=\"small\">"+bookmarkModel.getNotes()+"</li>");
					out.println("</ul></div>");
					out.println("<div class=\"col-md-3\">");
					out.println("<span class=\"pull-right\">");
					out.println("<button type=\"button\" class=\"btn btn-primary\" onclick=\"viewBookmark("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#bookmarkModal\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>");
					out.println("<button type=\"button\" class=\"btn btn-primary\" onclick=\"editBookmark("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#bookmarkModal\"><span class=\"glyphicon glyphicon-pencil\"></span></button>");
					out.println("<button type=\"button\" class=\"btn btn-primary\"><span class=\"glyphicon glyphicon-trash\"></span></button>");
					out.println("</span>");
					out.println("</div></div>");
					out.println("</div></div>");
				}
			}
		%>
	</div>
	
	<!-- Modal for bookmark -->
	<div id="bookmarkModal" class="modal fade" role="dialog">
		<div class="modal-dialog">
		    <!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 id="bookmarkTitle" class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<input type="hidden" value="" id="bookmarkId" /> <!-- these hidden field is used for saving the bookmark - if new bookmark then id is set to 0 -->
					<input type="hidden" value="" id="bookmarkVideoTitle" />
					<input type="hidden" value="" id="bookmarkYoutubeId" />
					<iframe id="bookmarkIframe" class="col-sm-offset-2" width="420" height="315"></iframe>
					<div class="row">
						<div class="col-md-2">
							<label for="query" class="control-label col-sm-2">Query: </label>
						</div>
						<div class="col-md-10">
							<label class="control-label" id="bookmarkQuery"></label>
						</div>
					</div>
					<label for="note" class="control-label col-sm-2">Notes: </label>
					<textarea class="form-control" rows="5" id="bookmarkNotes"></textarea>
				</div>
				<div class="modal-footer" id="bookmark-modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Discard</button>
					<button type="button" class="btn btn-primary" onclick="saveBookmark()">Save</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>