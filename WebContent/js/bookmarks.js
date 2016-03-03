/**
 * Retrieves segment information from Servlet and displays them in read-mode in modal
 */
function viewBookmark(bookmarkId) {
	$.ajax({
		url: "ViewBookmarkServlet?bookmarkId="+bookmarkId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateViewBookmarkModal(data);
		}
	});
}

/**
 * This function populates bookmarkModal in order to view the bookmark.
 * @param data - from the ViewBookmarkServlet response
 */
function populateViewBookmarkModal(data) {
	//no need for id as it is in view mode
	document.getElementById("bookmarkIframe").src = "https://www.youtube.com/embed/"+data.youtubeId+"?autoplay=1&enablejsapi=1";
	document.getElementById("bookmarkQuery").innerHTML = data.query;
	document.getElementById("bookmarkNotes").value = data.notes;
	var ul = document.getElementById("bookmark_keywords");
	ul.className = "list-inline";
	ul.innerHTML = ""; //clear the div
	for(var i=0; i<data.keywords.length; i++) {
		var li = document.createElement("li"); 
		//enclosing keyword in button for visual effect
		li.innerHTML = "<button type=\"button\" class=\"btn btn-default\">" + data.keywords[i].keyword + "</button>";
		li.setAttribute('data-id',data.keywords[i].id);
		li.setAttribute('data-value',data.keywords[i].keyword);
		ul.appendChild(li);
	}
	//modify modal title
	document.getElementById("bookmarkTitle").innerHTML = "View Bookmark - "+data.videoTitle;
	//make all fields read-only
	document.getElementById("bookmarkNotes").readOnly = true;
	//hide new keyword input field
	document.getElementById("bookmark_newkeyword").style.display = 'none';
	//no modal footer
	var footer = document.getElementById("bookmark-modal-footer");
	footer.style.display = "none";
}

/**
 * Retrieves segment information from Servlet and displays them in segment-mode in modal
 */
function editBookmark(bookmarkId, author) {
	$.ajax({
		url: "ViewBookmarkServlet?bookmarkId="+bookmarkId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateEditBookmarkModal(data);
		}
	});
}

/**
 * This function populates bookmarkModal in order to modify the bookmark.
 * @param data - from the ViewBookmarkServlet response
 */
function populateEditBookmarkModal(data) {
	//hidden fields are set so that there are no problems while persisting the data in the backend - no null pointer
	document.getElementById("bookmarkId").value = data.id;
	document.getElementById("bookmarkVideoTitle").value = data.title;
	document.getElementById("bookmarkYoutubeId").value = data.youtubeId;
	document.getElementById("bookmarkIframe").src = "https://www.youtube.com/embed/"+data.youtubeId+"?autoplay=1&enablejsapi=1";
	document.getElementById("bookmarkQuery").innerHTML = data.query;
	document.getElementById("bookmarkNotes").value = data.notes;
	var ul = document.getElementById("bookmark_keywords");
	ul.className = "list-inline";
	ul.innerHTML = ""; //clear the div
	for(var i=0; i<data.keywords.length; i++) {
		var li = document.createElement("li"); 
		li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + data.keywords[i].keyword + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeBookmarkKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
		li.setAttribute('data-value',data.keywords[i].keyword);
		li.setAttribute('data-id',data.keywords[i].id);
		ul.appendChild(li);
	}
	//modify modal title
	document.getElementById("bookmarkTitle").innerHTML = "Edit Bookmark - "+data.videoTitle;
	//make some fields read-only and others editable
	document.getElementById("bookmarkNotes").readOnly = false;
	//display new keyword input field
	document.getElementById("bookmark_newkeyword").style.display = 'inline';
	//show submit button
	var footer = document.getElementById("bookmark-modal-footer");
	footer.style.display = "block";
}

/**
 * This function saves the bookmark in database.
 * The segment can be new bookmark (from search.jsp) or an updated one.
 * The code in SaveBookmarkServlet handles it by checking if id is set to 0.
 */
function saveBookmark() {
	var id = document.getElementById("bookmarkId").value; //0 when saving new segment
	var title = document.getElementById("bookmarkVideoTitle").value;
	var youtubeId = document.getElementById("bookmarkYoutubeId").value;
	var query = document.getElementById("bookmarkQuery").innerHTML;
	var notes = document.getElementById("bookmarkNotes").value;
	var bookmark_keywords = document.getElementById("bookmark_keywords").childNodes;
	var keywords_list = ' "keywords" : [';
	for(var i=0; i<bookmark_keywords.length; i++) {
		var keyword = bookmark_keywords[i].getAttribute("data-value");
		var keyword_id = bookmark_keywords[i].getAttribute("data-id");
		var keywordData = '{ "id" : '+keyword_id+', "keyword":"'+keyword+'"}';
		if(i!=bookmark_keywords.length-1) {
			keywords_list = keywords_list + keywordData + ", ";
		} else {
			keywords_list = keywords_list + keywordData;
		}		
	}
	keywords_list = keywords_list + "]";
	
	var bookmarkData = '{ "id" : '+id+', "name":"'+name+'", "title":"'+title+'", "youtubeId":"'+youtubeId+'", "query":"'+query+'", "notes":"'+notes+'", '+keywords_list+'}';
	$.ajax({
		url: "SaveBookmarkServlet",
		type: 'POST',
		data: bookmarkData,
		dataType: 'text',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			$('#bookmarkModal').modal('hide');
			location.reload();
		}
	});
}

/**** Segments ****/
var segmentId = 0;
/**
 * This function retrieves the bookmark details
 */
function addVideo(bookmarkId) {
	$.ajax({
		url: "ViewBookmarkServlet?bookmarkId="+bookmarkId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateAddSegmentModal(data);
		}
	});
}
/**
 * This function displays the add segment modal for that bookmarked video
 * @param data
 */
function populateAddSegmentModal(data) {
	document.getElementById("segmentModalTitle").innerHTML = "Add Segment: "+data.videoTitle;
	document.getElementById("segmentId").value = 0; //new segment - set to 0 for servlet to recognize
	document.getElementById("segmentVideoTitle").value = data.videoTitle;
	document.getElementById("segmentYoutubeId").value = data.youtubeId;
	document.getElementById("segmentYoutubeTitle").innerHTML = ""; //no youtube title - youtube title is displayed in header to save space
	document.getElementById("segmentIframe").src = "https://www.youtube.com/embed/"+data.youtubeId+"?autoplay=1&enablejsapi=1";
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
	var ul = document.getElementById("segment_keywords");
	ul.className = "list-inline";
	ul.innerHTML = ""; //clear the div
	
	
	//display links for segments to library - if any
	document.getElementById("segments").innerHTML = "";
	//no add video link - the modal already shows add video
	if(data.addedToLibrary) {
		for(var index=0; index<data.segmentIds.length; index++) {
			var a = document.createElement("button");
			a.value = data.segmentIds[index];
			a.className = "btn btn-link";
			a.onclick = function(e) {
				segmentId = e.target.value;
				editVideo(data.id);
				return false;
		    }
			var text = document.createTextNode("Segment "+(index+1));
			a.appendChild(text);
			document.getElementById("segments").appendChild(a);
		}
	}
}

/**
 * This function retrieves the bookmarked video details.
 * @param bookmarkId
 */
function editVideo(bookmarkId) {
	$.ajax({
		url: "ViewBookmarkServlet?bookmarkId="+bookmarkId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateEditSegmentModal(data);
		}
	});
}

/**
 * This function displays the edit segment modal for the bookmarked video.
 */
function populateEditSegmentModal(bookmarkData) {
	if(segmentId == 0) {
		//get the first segment in the segmentIds list of video
		segmentId = bookmarkData.segmentIds[0];
	} else {
		//get the requested segment
	}
	//display the segment as saved in the database
	$.ajax({
		url: "ViewSegmentServlet?segmentId="+segmentId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			document.getElementById("segmentModalTitle").innerHTML = "Update Segment: "+data.title; //should be segment title? //get from ajax
			document.getElementById("segmentId").value = segmentId; //hidden fields used for saving segment
			document.getElementById("segmentVideoTitle").value = data.videoTitle;
			document.getElementById("segmentYoutubeId").value = data.youtubeId;
			document.getElementById("segmentYoutubeTitle").innerHTML = "YouTube Title: "+data.videoTitle;
			document.getElementById("segmentIframe").src = "https://www.youtube.com/embed/"+data.youtubeId+"?autoplay=1&enablejsapi=1";
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
			var ul = document.getElementById("segment_keywords");
			ul.className = "list-inline";
			ul.innerHTML = ""; //clear the div
			for(var i=0; i<data.keywords.length; i++) {
				var li = document.createElement("li"); 
				li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + data.keywords[i].keyword + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
				li.setAttribute('data-value',data.keywords[i].keyword);
				li.setAttribute('data-id',data.keywords[i].id);
				ul.appendChild(li);
			}
			
			//creating 'Add Segment' link
			document.getElementById("segments").innerHTML = "";
			var a = document.createElement("button");
			a.className = "btn btn-link";
			a.onclick = function(e) {
				addVideo(bookmarkData.id);
				return false;
		    }
			var text = document.createTextNode("Add Segment");
			a.appendChild(text);
			document.getElementById("segments").appendChild(a);
			document.getElementById("segments").appendChild(document.createElement("br"));
			//display the side segment links
			for(var index=0; index<bookmarkData.segmentIds.length; index++) {
				var a = document.createElement("button");
				a.value = bookmarkData.segmentIds[index];
				if(a.value == segmentId) { //disable the segment link if the modal shows the segment already
					a.className = "btn btn-link disabled";
				} else {
					a.className = "btn btn-link";
				}
				a.onclick = function(e) {
					segmentId = e.target.value;
					editVideo(bookmarkData.id);
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
 * This function saves the segment in database.
 * The segment can be new segment (from search.jsp) or an updated one.
 * The code in SaveSegmentServlet handles it by checking if id is set to 0.
 */
function saveSegment() {
	var id = document.getElementById("segmentId").value; //0 when saving new segment
	var name = document.getElementById("segmentName").value;
	var title = document.getElementById("segmentTitle").value;
	var videoTitle = document.getElementById("segmentVideoTitle").value; //not needed - as in edit segment the video already exists in database
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
	var url = document.getElementById("segmentUrl").value;
	var segment_keywords = document.getElementById("segment_keywords").childNodes;
	var keywords_list = ' "keywords" : [';
	for(var i=0; i<segment_keywords.length; i++) {
		var keyword = segment_keywords[i].getAttribute("data-value");
		var keyword_id = segment_keywords[i].getAttribute("data-id");
		var keywordData = '{ "id" : '+keyword_id+', "keyword":"'+keyword+'"}';
		if(i!=segment_keywords.length-1) {
			keywords_list = keywords_list + keywordData + ", ";
		} else {
			keywords_list = keywords_list + keywordData;
		}		
	}
	keywords_list = keywords_list + "]";
	
	//validate fields are not empty
	var domainIdIndex = document.getElementById("segmentDomain").selectedIndex;
	var topicIdIndex = document.getElementById("segmentTopic").selectedIndex;
	if(!name.trim().length || !title.trim().length || !description.trim().length || domainIdIndex == 0 || topicIdIndex == 0) {
		if(!name.trim().length) {
			document.getElementById("segmentName").style.borderColor = "red";
		}
		if(!title.trim().length) {
			document.getElementById("segmentTitle").style.borderColor = "red";
		}
		if(!description.trim().length) {
			document.getElementById("segmentDescription").style.borderColor = "red";
		}
		if(domainIdIndex == 0) {
			document.getElementById("segmentDomain").style.borderColor = "red";
		}
		if(topicIdIndex == 0) {
			document.getElementById("segmentTopic").style.borderColor = "red";
		}
		alert("Please fill in the empty fields.");
		return;
	}
	
	var json = '{ "id":'+id+', "name":"'+name+'", "title":"'+title+'", "videoTitle":"'+videoTitle+'", "youtubeId":"'+youtubeId
	+'", "startTime":'+startTime+', "endTime":'+endTime+', "topicId":'+topicId+', "description":"'+description+'", "author":"'+author
	+'", "version":"'+version+'", "created":"'+created+'", "modified":"'+modified+'", "url":"'+url+'", '+keywords_list+' }';
	
	$.ajax({
		url: "SaveSegmentServlet",
		type: 'POST',
		data: json,
		dataType: 'text',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			$('#segmentModal').modal('hide');
			location.reload();
		}
	});
}


/***** UTILS *****/
/**
 * This ensures the youtube video stops playing on dismissing the modal
 */
$('#bookmarkModal').on('hidden.bs.modal', function () {
	document.getElementById("bookmarkIframe").src = "";
});
$('#segmentModal').on('hidden.bs.modal', function () {
	//reset the bordercolor
	document.getElementById("segmentName").style.borderColor = "";
	document.getElementById("segmentTitle").style.borderColor = "";
	document.getElementById("segmentDescription").style.borderColor = "";
	document.getElementById("segmentDomain").style.borderColor = "";
	document.getElementById("segmentTopic").style.borderColor = "";
	//stop the video playback
	document.getElementById("segmentIframe").src = "";
	//reset segmentId
	segmentId = 0;
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

/**
 * For segments
 * This function adds the new keyword string into the list of all keywords.
 * On save, all the keywords in the list would be sent to the servlet.
 * @param newKeyword
 */
function addKeyword(newKeyword) {
	var ul = document.getElementById("segment_keywords");
	var li = document.createElement("li");
	li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + newKeyword.value + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
	li.setAttribute('data-id',0); //set new attribute as 0 so that it can be saved in database
	li.setAttribute('data-value',newKeyword.value);
	ul.appendChild(li);
	newKeyword.value = "";
}

/**
 * For segments
 * This function removes the list element on click of glyphicon-remove button.
 * @param keyword
 */
function removeKeyword(keyword) {
	keyword.parentNode.parentNode.parentNode.removeChild(keyword.parentNode.parentNode);
}

/**
 * For bookmarks
 * This function adds the new keyword string into the list of all keywords.
 * On save, all the keywords in the list would be sent to the servlet.
 * @param newKeyword
 */
function addBookmarkKeyword(newKeyword) {
	var ul = document.getElementById("bookmark_keywords");
	var li = document.createElement("li");
	li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + newKeyword.value + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeBookmarkKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
	li.setAttribute('data-id',0); //set new attribute as 0 so that it can be saved in database
	li.setAttribute('data-value',newKeyword.value);
	ul.appendChild(li);
	newKeyword.value = "";
}

/**
 * For bookmarks
 * This function removes the list element on click of glyphicon-remove button.
 * @param keyword
 */
function removeBookmarkKeyword(keyword) {
	keyword.parentNode.parentNode.parentNode.removeChild(keyword.parentNode.parentNode);
}