/**
 * filter parameters used
 */
var filter = "";
var durationFilter = "any";
var date = "";

/**
 * sets the duration as per user's choice in filter
 */
function filterDuration(dur){
	durationFilter = dur;
}

/**
 * sets the search query according to the filter value
 */
function filterResults(){
	//get all domains and append with the filter
	var domain = document.getElementsByName("domain");
	filter = "";
	for(var i=0; i<domain.length; i++){
		if(domain[i].checked){
			filter += domain[i].value + " ";
		}
	}
	//get all type and append with the filter
	var type = document.getElementsByName("type");
	for(var i=0; i<type.length; i++){
		if(type[i].checked){
			filter += type[i].value + " ";
		}
	}
	//get the datepicker value
	date = document.getElementById("datepicker").value;
	//call the SearchServlet
	window.vvm.searchYouTube();
}

/**
 * Knockout ViewModel for the Search Response list
 */
function videosViewModel(){
	var self = this;
	//array of videos in search result
	self.videos = ko.observableArray();
	//page tokens - previous page and next page
	self.prevPageToken = ko.observable();
	self.nextPageToken = ko.observable();
	self.pageToken = "";
	//Set segmentId to 0. When user clicks on segment link in add segment or edit segment modal
	//self.segmentId is set to the corresponding link's segmentId
	//This criteria is used in editSegment function to determine 
	//which segment - default first segment or the requested segment needs to be displayed in modal
	self.segmentId = 0;
	
	/**
	 * on click of Search button
	 * function to call the SearchServlet on query
	 */
	self.searchYouTube = function() {
		//check for the search type - whether search by query or by youtube link
		var searchTypes = document.getElementsByName("searchType");
		var searchType = "";
		for(var i=0; i<searchTypes.length; i++){
			if(searchTypes[i].checked){
				searchType = searchTypes[i].value;
				break;
			}
		}
		
		//set the request parameters - query, date, duration and page (or) youtubeid based on search type
		var url = "";
		var query = document.getElementById("query").value;
		if(searchType=="query"){
			if(filter)
				query = query+" "+filter;
			if(!date)
				date = "2000-01-01";
			url = "SearchServlet?query="+query+"&date="+date+"&page="+self.pageToken+"&duration="+durationFilter;
		}
		else if(searchType=="video"){
			//retrieve the youtube id from the youtube link
			var start = query.search("\\?v\\=")+3;
			var end = query.search("\\?v\\=")+14;
			url = "SearchServlet?youtubeid="+query.substring(start,end);
		}
		//check if there is any query to fetch results
		if(query.trim().length != 0) {
			$.ajax({
				url: url,
				type: 'GET',
				dataType: 'json',
				contentType: 'application/json',
				crossDomain: true,
				success: function(data) {
					//clear the list of models
					self.videos.removeAll();
					//set the page tokens based on response from youtube
					self.nextPageToken = data.nextPageToken;
					self.prevPageToken = data.prevPageToken;
					//set the list of models
					var searchVideoModelList = data.searchVideoModelList;
					for(var i=0; i<searchVideoModelList.length; i++) {
						var video = new VideoViewModel(searchVideoModelList[i].youtubeId,
								searchVideoModelList[i].title,searchVideoModelList[i].description,
								searchVideoModelList[i].publishedAt,searchVideoModelList[i].thumbnailUrl,
								searchVideoModelList[i].addedToBookmarks,searchVideoModelList[i].bookmarkId,
								searchVideoModelList[i].addedToLibrary,searchVideoModelList[i].segmentIds,
								searchVideoModelList[i].duration);
						self.videos.push(video);
					}
				},
				error: function(data) {
					alert("Something went wrong while getting search results. Please try again.");
				}
			});
			//reset page token for next search results
			self.pageToken = "";
		} else {
			alert("Please enter a query.");
		}
	}
	/**
	 * on click of previous page link
	 */
	self.getPrevPage = function() {
		self.pageToken = self.prevPageToken;
		self.searchYouTube();
	}
	/**
	 * on click of next page link
	 */
	self.getNextPage = function() {
		//console.log("in getNextPage()");
		self.pageToken = self.nextPageToken;
		self.searchYouTube();
	}
	/**
	 * on click of grid view button
	 */
	self.gridView = function() {
		document.getElementById("gridContainer").style.display = 'block';
		document.getElementById("detailedContainer").style.display = 'none';
	}
	/**
	 * on click of list view button
	 */
	self.listView = function() {
		document.getElementById("gridContainer").style.display = 'none';
		document.getElementById("detailedContainer").style.display = 'block';
	}
	/**
	 * on click of add bookmark button
	 */
	self.addBookmark = function(video) {
		document.getElementById("bookmarkId").value = 0; //new bookmark - set to 0 for servlet to recognize
		document.getElementById("bookmarkTitle").innerHTML = "Add Bookmark: "+video.title();
		document.getElementById("bookmarkVideoTitle").value = video.title();
		document.getElementById("bookmarkIframe").src = "https://www.youtube.com/embed/"+video.youtubeId()+"?autoplay=1&enablejsapi=1";
		document.getElementById("bookmarkYoutubeId").value = video.youtubeId();
		var query = document.getElementById("query").value+" "+filter;
		document.getElementById("bookmarkQuery").innerHTML = query.trim();
		document.getElementById("bookmarkNotes").value = "";	
	}
	/**
	 * on click of edit bookmark
	 */
	self.editBookmark = function(video) {
		document.getElementById("bookmarkId").value = video.bookmarkId();
		document.getElementById("bookmarkTitle").innerHTML = "Update Bookmark: "+video.title();
		document.getElementById("bookmarkVideoTitle").value = video.title();
		document.getElementById("bookmarkIframe").src = "https://www.youtube.com/embed/"+video.youtubeId()+"?autoplay=1&enablejsapi=1";
		document.getElementById("bookmarkYoutubeId").value = video.youtubeId();
		//display the query and notes as saved in the database
		$.ajax({
			url: "ViewBookmarkServlet?bookmarkId="+video.bookmarkId(),
			type: 'GET',
			dataType: 'json',
			contentType: "application/json",
			crossDomain: true,
			success: function(data) {
				document.getElementById("bookmarkQuery").innerHTML = data.query;
				document.getElementById("bookmarkNotes").value = data.notes;
			},
			error: function(data) {
				alert("Something went wrong while getting the bookmark. Please try again.");
			}
		});
	}
	/**
	 * on click of save to library button
	 */
	self.addVideo = function(video) {
		document.getElementById("segmentModalTitle").innerHTML = "Add Segment: "+video.title();
		document.getElementById("segmentId").value = 0; //new segment - set to 0 for servlet to recognize
		document.getElementById("segmentVideoTitle").value = video.title();
		document.getElementById("segmentYoutubeId").value = video.youtubeId();
		document.getElementById("segmentYoutubeTitle").innerHTML = ""; //no youtube title - youtube title is displayed in header to save space
		document.getElementById("segmentIframe").src = "https://www.youtube.com/embed/"+video.youtubeId()+"?autoplay=1&enablejsapi=1";
		document.getElementById("segmentName").value = "";
		document.getElementById("segmentName").readOnly = false;
		document.getElementById("segmentTitle").value = "";
		getAllDomains();
		document.getElementById("segmentDescription").value = "";
		document.getElementById("segmentAuthor").innerHTML = document.getElementById("username").innerHTML.trim();
		document.getElementById("segmentVersion").innerHTML = "0.0.1";
		current = new Date();
		document.getElementById("segmentCreated").innerHTML = getDateString(current);
		document.getElementById("segmentModified").innerHTML = getDateString(current);
		document.getElementById("segmentUrlDiv").style.display = 'none';
		//display links for segments to library - if any
		document.getElementById("segments").innerHTML = "";
		//no add video link - the modal already shows add video
		if(video.addedToLibrary()) {
			for(var index=0; index<video.segmentIds().length; index++) {
				var a = document.createElement("button");
				a.value = video.segmentIds()[index];
				a.className = "btn btn-link";
				a.onclick = function(e) {
					self.segmentId = e.target.value;
					self.editVideo(video);
					return false;
			    }
				var text = document.createTextNode("Segment "+(index+1));
				a.appendChild(text);
				document.getElementById("segments").appendChild(a);
			}
		}
	}
	/**
	 * on click of edit video in library button
	 */
	self.editVideo = function(video) {
		var segmentId = 0;
		if(self.segmentId == 0) {
			//get the first segment in the segmentIds list of video
			segmentId = video.segmentIds()[0];
		} else {
			//get the requested segment
			segmentId = self.segmentId;
		}
		//display the segment as saved in the database
		$.ajax({
			url: "ViewSegmentServlet?segmentId="+segmentId,
			type: 'GET',
			dataType: 'json',
			contentType: "application/json",
			crossDomain: true,
			success: function(data) {
				document.getElementById("segmentModalTitle").innerHTML = "Update Segment: "+video.title(); //should be segment title? //get from ajax
				document.getElementById("segmentId").value = segmentId; //hidden fields used for saving segment
				document.getElementById("segmentVideoTitle").value = video.title();
				document.getElementById("segmentYoutubeId").value = video.youtubeId();
				document.getElementById("segmentYoutubeTitle").innerHTML = "YouTube Title: "+video.title();
				document.getElementById("segmentIframe").src = "https://www.youtube.com/embed/"+video.youtubeId()+"?autoplay=1&enablejsapi=1";
				var valArray = [data.startTime, data.endTime]; //get the range value from ajax
				document.getElementById("range").value = valArray; //set the slider range values
				document.getElementById("segmentName").value = data.name; //no segment name - cannot be changed
				document.getElementById("segmentName").readOnly = true; //make the segment name readonly
				document.getElementById("segmentTitle").value = data.title; //get from ajax
				setDomainAndTopic(data.topicId); //set topic by ajax
				document.getElementById("segmentDescription").value = data.description; //get from ajax
				document.getElementById("segmentAuthor").innerHTML = data.author; //get from ajax --- multiple users modifying is allowed??? how do you keep track of all changes?
				document.getElementById("segmentVersion").innerHTML = data.version; //get from ajax
				document.getElementById("segmentCreated").innerHTML = data.created; //get from ajax
				document.getElementById("segmentModified").innerHTML = data.modified; //get from ajax 
				document.getElementById("segmentUrlDiv").style.display = 'block'; //get value from ajax
				document.getElementById("segmentUrl").innerHTML = data.url; //get from ajax
				//creating 'Add Segment' link
				document.getElementById("segments").innerHTML = "";
				var a = document.createElement("button");
				a.className = "btn btn-link";
				a.onclick = function(e) {
					self.addVideo(video);
					return false;
			    }
				var text = document.createTextNode("Add Segment");
				a.appendChild(text);
				document.getElementById("segments").appendChild(a);
				document.getElementById("segments").appendChild(document.createElement("br"));
				//display the side segment links
				for(var index=0; index<video.segmentIds().length; index++) {
					var a = document.createElement("button");
					a.value = video.segmentIds()[index];
					if(a.value == segmentId) { //disable the segment link if the modal shows the segment already
						a.className = "btn btn-link disabled";
					} else {
						a.className = "btn btn-link";
					}
					a.onclick = function(e) {
						self.segmentId = e.target.value;
						self.editVideo(video);
						return false;
				    }
					var text = document.createTextNode("Segment "+(index+1));
					a.appendChild(text);
					document.getElementById("segments").appendChild(a);
					document.getElementById("segments").appendChild(document.createElement("br"));
				}
			},
			error: function(data) {
				alert("Something went wrong while getting the segment. Please try again.");
			}
		});
	}
	/**
	 * on click of play video button
	 */
	self.playVideo = function(video) {
		//TODO add a link to view this video on youtube https://www.youtube.com/watch?v=
		document.getElementById("playVideoIframe").src = "https://www.youtube.com/embed/"+video.youtubeId()+"?autoplay=1&enablejsapi=1";
		document.getElementById("playVideoTitle").innerHTML = video.title();
	}
}
/**
 * View Model for SearchVideoModel in backend
 */
function VideoViewModel(youtubeId,title,description,publishedAt,thumbnailUrl,addedToBookmarks,bookmarkId,addedToLibrary,segmentIds,duration) {
	var self = this;
	self.youtubeId = ko.observable(youtubeId);
	self.title = ko.observable(title);
	self.description = ko.observable(description);
	self.publishedAt = ko.observable(publishedAt);
	self.thumbnailUrl = ko.observable(thumbnailUrl);
	self.addedToBookmarks = ko.observable(addedToBookmarks);
	self.bookmarkId = ko.observable(bookmarkId);
	self.addedToLibrary = ko.observable(addedToLibrary);
	self.segmentIds = ko.observableArray(segmentIds);
	self.duration = ko.observable(duration);
}

window.vvm = new videosViewModel();
ko.applyBindings(window.vvm,$("#resultsContainer")[0]);

/**
 * This function is used to save or update a bookmark in database
 */
function saveBookmark() {
	//check if all mandatory fields are filled
	var id = document.getElementById("bookmarkId").value;
	var videoTitle = document.getElementById("bookmarkVideoTitle").value;
	var youtubeId = document.getElementById("bookmarkYoutubeId").value;
	var query = document.getElementById("bookmarkQuery").innerHTML;
	var notes = document.getElementById("bookmarkNotes").value;
	//save the bookmark
	var json = JSON.stringify({id: id, videoTitle:videoTitle, youtubeId: youtubeId, query: query, notes: notes });
	$.ajax({
		url: "SaveBookmarkServlet",
		type: 'POST',
		dataType: 'text',
		contentType: "application/json",
		data: json,
		crossDomain: true,
		success: function(data) {
			if(data == "success") {
				//update the page results by calling search again
				window.vvm.searchYouTube();
				//dismiss the modal
				$('#bookmarkModal').modal('hide')
			}
		}, 
		error: function(data) {
			alert("Something went wrong while saving the bookmark. Please try again later.");
		}
	});
}

function saveSegment() {
	//check if all mandatory fields are filled
	var id = document.getElementById("segmentId").value;
	var name = document.getElementById("segmentName").value;
	var title = document.getElementById("segmentTitle").value;
	var videoTitle = document.getElementById("segmentVideoTitle").value;
	var youtubeId = document.getElementById("segmentYoutubeId").value;
	var value = document.getElementById("range").value.split(",");
	var startTime = value[0];
	var endTime = value[1];
	var topicId = document.getElementById("segmentTopic").value;
	var description = document.getElementById("segmentDescription").value;
	var author = document.getElementById("segmentAuthor").innerHTML;
	var version = document.getElementById("segmentVersion").innerHTML;
	var created = document.getElementById("segmentCreated").innerHTML;
	var modified = document.getElementById("segmentModified").innerHTML;
	
	//save the segment
	var json = JSON.stringify({id: id, name: name, title: title, videoTitle: videoTitle, youtubeId: youtubeId, 
		startTime: startTime, endTime: endTime, topicId: topicId, description: description, author: author, 
		version: version, created: created, modified: modified  });
	$.ajax({
		url: "SaveSegmentServlet",
		type: 'POST',
		dataType: 'text',
		contentType: "application/json",
		data: json,
		crossDomain: true,
		success: function(data) {
			if(data == "success") {
				//update the page results by calling search again
				window.vvm.searchYouTube();
				//dismiss the modal
				$('#segmentModal').modal('hide')
			}
		}, 
		error: function(data) {
			alert("Something went wrong while saving the segment. Please try again later.");
		}
	});
}

/***** UTIL *****/
/**
 * This ensures the youtube video stops playing on dismissing the modal
 */
$('#playVideoModal').on('hidden.bs.modal', function () {
	document.getElementById("playVideoIframe").src = "";
});
$('#bookmarkModal').on('hidden.bs.modal', function () {
	document.getElementById("bookmarkIframe").src = "";
});
$('#segmentModal').on('hidden.bs.modal', function () {
	document.getElementById("segmentIframe").src = "";
});
/**
 * This function returns a date string
 */
function getDateString(d) {
	var m_names = new Array("Jan", "Feb", "Mar", 
		"Apr", "May", "Jun", "Jul", "Aug", "Sep", 
		"Oct", "Nov", "Dec");
	var curr_date = d.getDate();
	var curr_month = d.getMonth();
	var curr_year = d.getFullYear();
	return curr_date+" "+m_names[curr_month]+" "+curr_year;
}
/**
 * This function sets a list of domains
 */
var domains;
function getAllDomains() {
	domains = [];
	//get the domain drop-down and reset it
	var segmentDomain = document.getElementById("segmentDomain");
	segmentDomain.options.length = 0;
	var defaultOption = document.createElement("option");
	defaultOption.text = "Select domain";
	defaultOption.selected = "selected";
	defaultOption.disabled = "disabled";
	segmentDomain.add(defaultOption);
	
	//get the topic drop-down and reset it
	var segmentTopic = document.getElementById("segmentTopic");
	segmentTopic.options.length = 0;
	var defaultOption = document.createElement("option");
	defaultOption.text = "Select topic";
	defaultOption.selected = "selected";
	defaultOption.disabled = "disabled";
	segmentTopic.add(defaultOption);
	
	//get all domains and topics from the servlet
	$.ajax({
		url: "ViewAllDomainsServlet",
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		crossDomain: true,
		async: false,
		success: function(data) {
			if(data != null) {
				for(var index=0; index<data.length; index++) {
					//set the domain object
					var domainObject = {
							id : data[index].id,
							title : data[index].title,
							topics : []
					};
					//set the topics array in domain object
					for(var nestedIndex=0; nestedIndex<data[index].topics.length; nestedIndex++) {
						var topicObject = {
								id : data[index].topics[nestedIndex].id,
								title : data[index].topics[nestedIndex].title
						}
						domainObject.topics.push(topicObject);
					}
					
					//populate the domain drop down
					var option = document.createElement("option");
					option.text = domainObject.title;
					option.value = domainObject.id;
					segmentDomain.add(option);
					
					//add the domain object to the domains array
					domains.push(domainObject);
				}
			}
		},
		error: function(data) {
			alert("Something went wrong in retrieving the domains. Please try again later.");
		}
	});
}
/**
 * This function populates the topic drop down based on the domain value selected
 * @param domainId
 */
function getAllTopics(domainId) {
	//find the domain object by the domain id
	var resultDomain = $.grep(domains, function(e){ return e.id == domainId; });
	
	if(resultDomain.length == 1) {
		//populate the topic drop down
		var segmentTopic = document.getElementById("segmentTopic");
		for(var index=0; index<resultDomain[0].topics.length; index++) {
			var option = document.createElement("option");
			option.text = resultDomain[0].topics[index].title;
			option.value = resultDomain[0].topics[index].id;
			segmentTopic.add(option);
		}
	} else {
		alert("Something went wrong in retrieving the topics. Please try again later.");
	}
}
/**
 * This function sets the domain and topic values in dropdown based on the topicId
 * @param topicId
 */
function setDomainAndTopic(topicId) {
	getAllDomains(); //get all domainModels
	for(var index=0; index<domains.length; index++) {
		var topics = domains[index].topics;
		for(var nestedIndex=0; nestedIndex<topics.length; nestedIndex++) {
			if(topics[nestedIndex].id == topicId) {
				//set the domain and topic drop down is the values match
				//make sure domain is selected first - else error in setting topics as it would be empty
				document.getElementById("segmentDomain").value = domains[index].id;
				getAllTopics(domains[index].id);
				document.getElementById("segmentTopic").value = topicId;
			}
		}
	}
}