<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="edu.pitt.sis.exp.educvideos.videoauthoring.models.BookmarkModel" %>

<!DOCTYPE html>
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
<!-- Slider related jQuery and CSS - referenced from http://seiyria.com/bootstrap-slider/ -->
<link rel="stylesheet" href="css/index.css">
<link rel="stylesheet" href="css/bootstrap-slider.css">
<script src="js/bootstrap-slider.js"></script>
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
					<li><a href="#">Welcome <span id="username"><% 
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
					} catch(Exception e) {} %></span></a></li>
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
					out.println("<div class=\"col-md-3\"><button type=\"button\" class=\"btn btn-link\" onclick=\"viewBookmark("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#bookmarkModal\"><img src=\"http://img.youtube.com/vi/"+bookmarkModel.getYoutubeId()+"/default.jpg\" class=\"img-thumbnail\" width=\"152\" height=\"118\" /></button></div>");
					out.println("<div class=\"col-md-6\">");
					out.println("<ul class=\"list-unstyled\">");
					out.println("<li><button type=\"button\" class=\"btn btn-link\" onclick=\"viewBookmark("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#bookmarkModal\"><h4>"+bookmarkModel.getVideoTitle()+"</h4></button></li>");
					out.println("<li>"+bookmarkModel.getQuery()+"</li>");
					out.println("<li class=\"small\">"+bookmarkModel.getNotes()+"</li>");
					out.println("</ul></div>");
					out.println("<div class=\"col-md-3\">");
					out.println("<span class=\"pull-right\">");
					out.println("<button type=\"button\" class=\"btn btn-primary\" onclick=\"viewBookmark("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#bookmarkModal\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>");
					out.println("<button type=\"button\" class=\"btn btn-primary\" onclick=\"editBookmark("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#bookmarkModal\"><span class=\"glyphicon glyphicon-pencil\"></span></button>");
					if(bookmarkModel.getAddedToLibrary()) {
						out.println("<button type=\"button\" class=\"btn btn-success\" onclick=\"editVideo("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#segmentModal\"><span class=\"glyphicon glyphicon-floppy-saved\"></span></button>");
					} else {
						out.println("<button type=\"button\" class=\"btn btn-primary\" onclick=\"addVideo("+bookmarkModel.getId()+")\" data-toggle=\"modal\" data-target=\"#segmentModal\"><span class=\"glyphicon glyphicon-floppy-disk\"></span></button>");
					}
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
					<div class="row">
						<div class="col-md-1">
							<label for="bookmark_keywords">Keywords:</label>
						</div>
						<div class="col-md-4 col-md-offset-1">
							<input id="bookmark_newkeyword" type="text" name="bookmark_newkeyword" placeholder="Press enter to add new keyword" class="form-control" onchange="addBookmarkKeyword(this)" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<ul id="bookmark_keywords">
							</ul>
						</div>
					</div>
				</div>
				<div class="modal-footer" id="bookmark-modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Discard</button>
					<button type="button" class="btn btn-primary" onclick="saveBookmark()">Save</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Modal for segment -->
	<div id="segmentModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="segmentModalTitle">Add/Update Segment: Segment Title (Segment Name)</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-8">
							<input type="hidden" id="segmentId" value=""><!-- hidden field used for saving segment - if value 0 then save a new segment -->
							<input type="hidden" id="segmentVideoTitle" value=""><!-- youtube title  because the youtube title below say 'youtube title'; don't want to sit and extract -->
							<input type="hidden" id="segmentYoutubeId" value="">
							<h4 id="segmentYoutubeTitle">Youtube Title</h4>
							<iframe id="segmentIframe" class="col-sm-offset-2" width="280" height="210"></iframe>
							<div id="segment_buttons" class="row">
								<div class="col-md-12">
									<button type="button" class="btn btn-primary active" onclick="startSegment()">Set Start of Video Segment</button>
									<button type="button" class="btn btn-primary pull-right" onclick="endSegment()">Set End of Video Segment</button>
								</div>
							</div>
							<div id="slider" class="row">
								<div class="col-md-2">
									<label for="slider" class="control-label">Time: </label>
								</div>
								<div class="col-md-10">
									<input type="text" id="range" data-slider-tooltip="always" data-slider-handle="triangle" />
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<label for="segmentName" class="control-label">Name: </label>
								</div>
								<div class="col-md-10">
									<input type="text" id="segmentName" name="segmentName" class="form-control" placeholder="Name of Video">
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<label for="segmentTitle" class="control-label">Title: </label>
								</div>
								<div class="col-md-10">
									<input type="text" id="segmentTitle" name="segmentTitle" class="form-control" placeholder="Title of Video">
								</div>
							</div>
							<label for="segmentDescription" class="control-label">Description: </label>
							<textarea class="form-control" rows="3" id="segmentDescription"></textarea>
						</div>
						<div class="col-md-4">
							<div id="segments">
							</div><br /><br />
							<div class="row">
								<div class="col-md-4">
									<label for="segmentDomain" class="control-label">Domain:</label>
								</div>
								<div class="col-md-8">
									<select class="form-control" id="segmentDomain" onchange="getAllTopics(this.value);">
										<option selected disabled>Select domain</option>
									</select>
								</div>
							</div><br />
							<div class="row">
								<div class="col-md-4">
									<label for="segmentTopic" class="control-label">Topic:</label>
								</div>
								<div class="col-md-8">
									<select class="form-control" id="segmentTopic">
										<option selected disabled>Select topic</option>
									</select>
								</div>
							</div><br />
							<div class="row">
								<div class="col-md-4">
									<label for="segmentAuthor" class="control-label">Author:</label>
								</div>
								<div class="col-md-8">
									<label class="control-label" id="segmentAuthor"></label>
								</div>
							</div><br />
							<div class="row">
								<div class="col-md-4">
									<label for="segmentVersion" class="control-label">Version:</label>
								</div>
								<div class="col-md-8">
									<label class="control-label" id="segmentVersion"></label>
								</div>
							</div><br />
							<div class="row">
								<div class="col-md-4">
									<label for="segmentCreated" class="control-label">Created:</label>
								</div>
								<div class="col-md-8">
									<label class="control-label" id="segmentCreated"></label>
								</div>
							</div><br />
							<div class="row">
								<div class="col-md-4">
									<label for="segmentModified" class="control-label">Last modified:</label>
								</div>
								<div class="col-md-8">
									<label class="control-label" id="segmentModified"></label>
								</div>
							</div><br />
							<div class="row" id="segmentUrlDiv">
								<div class="col-md-2">
									<label for="segmentUrl" class="control-label">URL:</label>
								</div>
								<div class="col-md-10">
									<label class="control-label" id="segmentUrl"></label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-1">
							<label for="segment_keywords">Keywords:</label>
						</div>
						<div class="col-md-4 col-md-offset-1">
							<input id="segment_newkeyword" type="text" name="segment_newkeyword" placeholder="Press enter to add new keyword" class="form-control" onchange="addKeyword(this)" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<ul id="segment_keywords">
							</ul>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary" onclick="saveSegment()">Save</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Adding the corresponding settings javascript file -->
	<script src="js/bookmarks.js"></script>
	
	<!-- Bootstrap slider -->
    <script type="text/javascript">
		var s = new Slider("#range", { id: "slider12c", min: 0, max: 10, range: true, value: [0,0], tooltip: 'always', tooltip_split: true });
			
		var tag = document.createElement('script');
		tag.src = "https://www.youtube.com/iframe_api";
		var firstScriptTag = document.getElementsByTagName('script')[0];
		firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
	
		var player, duration, stopPlayTimer, stopPlayAt;
		
		function onYouTubeIframeAPIReady() {
			  player = new YT.Player('segmentIframe', {
			    events: {
			      'onReady': onPlayerReady
			    }
			  });
		}
		
		function onPlayerReady() {		
		  duration = player.getDuration();
		  s.options.max = duration;
		  var valueStrArray = document.getElementById("range").value.split(",");
		  var valueArray = [parseInt(valueStrArray[0]),parseInt(valueStrArray[1])];
		  s.setValue(valueArray,true,true);
		  player.seekTo(valueArray[0],true);
		  console.log("player is ready!"+valueStrArray);
		  //onStateChange event is fired only once when the modal is shown
		  //therefore using a timer to ensure the onStateChange event is fired everytime the player's state is changed.
		  var playerState = player.getPlayerState();
		  playerStateInterval = setInterval( function() {
			  var state = player.getPlayerState();
			  if ( playerState !== state ) {
			    onPlayerStateChange( {
			      data: state
			    });
			    playerState = state;
			  }
			}, 10);
		}
	
		function onPlayerStateChange(event) {
		  	if (event.data == YT.PlayerState.PLAYING) {
		  		var time, rate, remainingTime;
			  	time = player.getCurrentTime();
			  	stopPlayAt = parseInt(document.getElementById("range").value.split(",")[1]);
			  	if (time < stopPlayAt) {
					rate = player.getPlaybackRate();
					remainingTime = (stopPlayAt - time) / rate;
					stopPlayTimer = setInterval(pauseVideo, (remainingTime+1) * 1000);
			  	}
		  	}
		  	if(event.data == YT.PlayerState.PAUSED || event.data == YT.PlayerState.ENDED) {
				//stop the interval to pause the player at maximum
				clearInterval(stopPlayTimer);
		  	}
		}
	
		function pauseVideo(){
			player.pauseVideo();
		}
		
		s.on("slideStart", function(val){
			player.pauseVideo();
		});
		s.on("slideStop", function(val){
			if(minChanged)
				player.seekTo(val[0],true);
			//if maxHandle changes and it stops sliding then seek to 10 secs before the max handle and play from there
			else
				player.seekTo(val[1]-10,true);
			player.playVideo();
			minChanged = false;
		});
		s.on("change", function(data){
			//if the minimum is changed seek the player to new value of minimum
			if(data.oldValue[0]!=data.newValue[0]){
				player.seekTo(data.newValue[0],false);
				//this ensures that the seekTo function of the player can set 
				//the allowSeekAhead as true when the slideStop event occurs.
				minChanged = true;
			}
			//if maximum is changed then reset the timer so that the video pauses at the maximum
			else{
				minChanged = false;
			}
		});
		
		function startSegment(){
			var startTime = parseInt(player.getCurrentTime());
			var valueStrArray = document.getElementById("range").value.split(",");
			var valueArray = [startTime,parseInt(valueStrArray[1])];
			s.setValue(valueArray,true,true);	
		}
		function endSegment(){
			var endTime = parseInt(player.getCurrentTime());
			var valueStrArray = document.getElementById("range").value.split(",");
			var valueArray = [parseInt(valueStrArray[0]),endTime];
			s.setValue(valueArray,true,true);	
		}
		
		//Stop the video from playing when the modal is closed.
		$('#myModal').on('hidden.bs.modal', function () {
			player.stopVideo();
			//stop the interval checking the player state
			clearInterval(playerStateInterval);
			//stop the interval to pause the player at maximum
			clearInterval(stopPlayTimer);
		    document.getElementById("videoframe").removeAttribute("src");
		    console.log("closing modal"+document.getElementById("videoframe").src+".");
		});
		</script>
</body>
</html>