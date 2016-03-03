/**
 * Retrieves segment information from Servlet and displays them in read-mode in modal
 */
function viewSegment(segmentId) {
	$.ajax({
		url: "ViewSegmentServlet?segmentId="+segmentId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateViewSegmentModal(data);
			$("#segmentModal").modal("show");
		}
	});
}

/**
 * This function populates segmentModal in order to view the segment.
 * @param data - from the ViewSegmentServlet response
 */
function populateViewSegmentModal(data) {
	//no need for id as it is in view mode
	document.getElementById("segmentYoutubeTitle").innerHTML = "YouTube Title: "+data.videoTitle;
	document.getElementById("segmentName").value = data.name;
	document.getElementById("segmentTitle").value = data.title;
	document.getElementById("segmentIframe").src = "https://www.youtube.com/embed/"+data.youtubeId+"?autoplay=1&enablejsapi=1&start="+data.startTime+"&end="+data.endTime; 
	setDomainAndTopic(data.topicId);
	document.getElementById("segmentDescription").value = data.description;
	document.getElementById("segmentAuthor").innerHTML = data.author;
	document.getElementById("segmentVersion").innerHTML = data.version;
	document.getElementById("segmentCreated").innerHTML = data.created;
	document.getElementById("segmentModified").innerHTML = data.modified;
	document.getElementById("segmentUrl").innerHTML = data.url;
	//modify modal title
	document.getElementById("segmentModalTitle").innerHTML = "View Video - "+data.title;
	//make all fields read-only
	document.getElementById("segmentName").readOnly = true;
	document.getElementById("segmentTitle").readOnly = true;
	document.getElementById("segmentDescription").readOnly = true;
	document.getElementById("segmentDomain").readOnly = true;
	document.getElementById("segmentTopic").readOnly = true;
	//no slider and slider buttons
	document.getElementById("segment_buttons").style.display = 'none';
	document.getElementById("slider").style.display = 'none';
	//no footer
	var footer = document.getElementById("footer");
	footer.style.display = 'none';
}

/**
 * Retrieves segment information from Servlet and displays them in segment-mode in modal
 */
function editSegment(segmentId) {
	$.ajax({
		url: "ViewSegmentServlet?segmentId="+segmentId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateEditSegmentModal(data);
			$("#segmentModal").modal("show");
		}
	});
}

/**
 * This function populates segmentModal in order to modify the segment.
 * @param data - from the ViewSegmentServlet response
 */
function populateEditSegmentModal(data) {
	document.getElementById("segmentId").value = data.id;
	document.getElementById("segmentVideoTitle").value = data.videoTitle;
	document.getElementById("segmentYoutubeId").value = data.youtubeId;
	document.getElementById("segmentYoutubeTitle").innerHTML = "YouTube title: "+data.videoTitle;
	document.getElementById("segmentName").value = data.name;
	document.getElementById("segmentTitle").value = data.title;
	document.getElementById("segmentIframe").src = "https://www.youtube.com/embed/"+data.youtubeId+"?autoplay=1&enablejsapi=1";
	
	//show slider and slider buttons
	document.getElementById("segment_buttons").style.display = 'block';
	document.getElementById("slider").style.display = 'none';
	var valArray = [data.startTime, data.endTime]; //get the range value from ajax
	document.getElementById("range").value = valArray; //set the slider range values
	
	setDomainAndTopic(data.topicId);
	document.getElementById("segmentDescription").value = data.description;
	document.getElementById("segmentAuthor").innerHTML = data.author;
	document.getElementById("segmentVersion").innerHTML = data.version;
	document.getElementById("segmentCreated").innerHTML = data.created;
	document.getElementById("segmentModified").innerHTML = data.modified; //will set to current date and time if user clicks save
	document.getElementById("segmentUrl").innerHTML = data.url;
	
	//modify modal title
	document.getElementById("segmentModalTitle").innerHTML = "Edit Video - "+data.title;
	
	//make some fields read-only and others editable
	document.getElementById("segmentName").readOnly = true;
	document.getElementById("segmentTitle").readOnly = false;
	document.getElementById("segmentDescription").readOnly = false;
	
	//show footer
	var footer = document.getElementById("footer");
	footer.style.display = 'block';
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
	
	var json = JSON.stringify({id: id, name: name, title: title, videoTitle: videoTitle, youtubeId: youtubeId, 
		startTime: startTime, endTime: endTime, topicId: topicId, description: description, author: author, 
		version: version, created: created, modified: modified  });
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


/****** UTILS ******/
/**
 * This ensures the youtube video stops playing on dismissing the modal
 */
$('#segmentModal').on('hidden.bs.modal', function () {
	document.getElementById("segmentIframe").src = "";
});
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