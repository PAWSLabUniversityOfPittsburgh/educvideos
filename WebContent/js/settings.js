/*****Domain Related*****/

/**
 * Retrieves domain information from Servlet and displays them in read-mode in modal
 */
function viewDomain(domainId) {
	$.ajax({
		url: "ViewDomainServlet?domainId="+domainId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateViewDomainModal(data);
			$("#domainModal").modal("show");
		}
	});
}

/**
 * This function populates domainModal in order to view the domain.
 * @param data - from the ViewDomainServlet response
 */
function populateViewDomainModal(data) {
	//no need for id as it is in view mode
	document.getElementById("domain_name").value = data.name;
	document.getElementById("domain_title").value = data.title;
	document.getElementById("domain_description").value = data.description;
	document.getElementById("domain_author").value = data.author;
	document.getElementById("domain_license").value = data.license;
	document.getElementById("domain_version").value = data.version;
	document.getElementById("domain_created").value = data.created;
	document.getElementById("domain_modified").value = data.modified;
	var ul = document.getElementById("domain_keywords");
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
	document.getElementById("domain-modal-title").innerHTML = "View Domain - "+data.title;
	//make all fields read-only
	document.getElementById("domain_name").readOnly = true;
	document.getElementById("domain_title").readOnly = true;
	document.getElementById("domain_description").readOnly = true;
	document.getElementById("domain_author").readOnly = true;
	document.getElementById("domain_license").readOnly = true;
	document.getElementById("domain_version").readOnly = true;
	document.getElementById("domain_created").readOnly = true;
	document.getElementById("domain_modified").readOnly = true;
	//hide new keyword input field
	document.getElementById("domain_newkeyword").style.display = 'none';
	//no submit button
	var submit = document.getElementById("domain-modal-submit");
	submit.style.display = "none";
}

/**
 * Retrieves domain information from Servlet and displays them in edit-mode in modal
 */
function editDomain(domainId) {
	$.ajax({
		url: "ViewDomainServlet?domainId="+domainId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateEditDomainModal(data);
			$("#domainModal").modal("show");
		}
	});
}

/**
 * This function populates domainModal in order to modify the domain.
 * @param data - from the ViewDomainServlet response
 */
function populateEditDomainModal(data) {
	document.getElementById("domain_id").value = data.id;
	document.getElementById("domain_name").value = data.name;
	document.getElementById("domain_title").value = data.title;
	document.getElementById("domain_description").value = data.description;
	document.getElementById("domain_author").value = data.author;
	document.getElementById("domain_license").value = data.license;
	document.getElementById("domain_version").value = data.version;
	document.getElementById("domain_created").value = data.created;
	document.getElementById("domain_modified").value = data.modified; //will set to current date and time if user clicks save 
	var ul = document.getElementById("domain_keywords");
	ul.className = "list-inline";
	ul.innerHTML = ""; //clear the div
	for(var i=0; i<data.keywords.length; i++) {
		var li = document.createElement("li"); 
		li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + data.keywords[i].keyword + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
		li.setAttribute('data-value',data.keywords[i].keyword);
		li.setAttribute('data-id',data.keywords[i].id);
		ul.appendChild(li);
	}
	//modify modal title
	document.getElementById("domain-modal-title").innerHTML = "Edit Domain - "+data.title;
	//make all fields read-only
	document.getElementById("domain_name").readOnly = true;
	document.getElementById("domain_title").readOnly = false;
	document.getElementById("domain_description").readOnly = false;
	document.getElementById("domain_author").readOnly = true;
	document.getElementById("domain_license").readOnly = false;
	document.getElementById("domain_version").readOnly = false;
	document.getElementById("domain_created").readOnly = true;
	document.getElementById("domain_modified").readOnly = true;
	//display new keyword input field
	document.getElementById("domain_newkeyword").style.display = 'inline';
	//show submit button
	var submit = document.getElementById("domain-modal-submit");
	submit.style.display = "inline";
}

/**
 * This function saves the domain in database.
 * The domain can be new domain or an updated one.
 */
function saveDomain() {
	var id = document.getElementById("domain_id").value; //null when saving new domain
	var name = document.getElementById("domain_name").value;
	var title = document.getElementById("domain_title").value;
	var description = document.getElementById("domain_description").value;
	var author = document.getElementById("domain_author").value;
	var license = document.getElementById("domain_license").value;
	var version = document.getElementById("domain_version").value;
	var created = document.getElementById("domain_created").value;
	var modified = document.getElementById("domain_modified").value;
	var domain_keywords = document.getElementById("domain_keywords").childNodes;
	var keywords_list = ' "keywords" : [';
	for(var i=0; i<domain_keywords.length; i++) {
		var keyword = domain_keywords[i].getAttribute("data-value");
		var keyword_id = domain_keywords[i].getAttribute("data-id");
		var keywordData = '{ "id" : '+keyword_id+', "keyword":"'+keyword+'"}';
		if(i!=domain_keywords.length-1) {
			keywords_list = keywords_list + keywordData + ", ";
		} else {
			keywords_list = keywords_list + keywordData;
		}		
	}
	keywords_list = keywords_list + "]";
	
	var domainData = '{ "id" : '+id+', "name":"'+name+'", "title":"'+title+'", "description":"'+description+'", "author":"'+author+'", "license":"'+license+'", "version":"'+version+'", "created":"'+created+'", "modified":"'+modified+'", '+keywords_list+'}';
	$.ajax({
		url: "SaveDomainServlet",
		type: 'POST',
		data: domainData,
		dataType: 'text',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			$('#domainModal').modal('hide');
			location.reload();
		}
	});
}

/**
 * This function ensures the domainModal can add a new domain to database.
 */
function populateAddDomainModal(author, current_time) {
	//modify modal title
	document.getElementById("domain-modal-title").innerHTML = "Add new domain";
	//reset all form values - this is needed when user opens view or edit domain and then clicks add domain
	document.getElementById("domain_id").value = 0; //set to 0 so that servlet can identify whether to save domain or update it
	document.getElementById("domain_name").value = '';
	document.getElementById("domain_title").value = '';
	document.getElementById("domain_description").value = '';
	document.getElementById("domain_author").value = author;
	document.getElementById("domain_license").value = '';
	document.getElementById("domain_version").value = '';
	document.getElementById("domain_created").value = current_time; //set to current date and time
	document.getElementById("domain_modified").value = current_time; //set to current date and time
	var ul = document.getElementById("domain_keywords");
	ul.innerHTML = ""; //clear the div
	//make all fields are not read-only
	document.getElementById("domain_name").readOnly = false;
	document.getElementById("domain_title").readOnly = false;
	document.getElementById("domain_description").readOnly = false;
	document.getElementById("domain_author").readOnly = true;
	document.getElementById("domain_license").readOnly = false;
	document.getElementById("domain_version").readOnly = false;
	document.getElementById("domain_created").readOnly = true;
	document.getElementById("domain_modified").readOnly = true;
	//display new keyword input field
	document.getElementById("domain_newkeyword").style.display = 'inline';
	//show submit button
	var submit = document.getElementById("domain-modal-submit");
	submit.style.display = "inline";
}





/*****Topic Related*****/

/**
 * Retrieves domain information from Servlet and displays them in read-mode in modal
 */
function viewTopic(topicId) {
	$.ajax({
		url: "ViewTopicServlet?topicId="+topicId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateViewTopicModal(data);
			$("#topicModal").modal("show");
		}
	});
}

/**
 * This function populates domainModal in order to view the domain.
 * @param data - from the ViewDomainServlet response
 */
function populateViewTopicModal(data) {
	//no need for id as it is in view mode
	document.getElementById("topic_title").value = data.title;
	document.getElementById("topic_description").value = data.description;
	var ul = document.getElementById("topic_keywords");
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
	document.getElementById("topic-modal-title").innerHTML = "View Topic - "+data.title;
	//make all fields read-only
	document.getElementById("topic_title").readOnly = true;
	document.getElementById("topic_description").readOnly = true;
	//hide new keyword input field
	document.getElementById("topic_newkeyword").style.display = 'none';
	//no submit button
	var submit = document.getElementById("topic-modal-submit");
	submit.style.display = "none";
}

/**
 * Retrieves topic information from Servlet and displays them in edit-mode in modal
 */
function editTopic(topicId) {
	$.ajax({
		url: "ViewTopicServlet?topicId="+topicId,
		type: 'GET',
		dataType: 'json',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			populateEditTopicModal(data);
			$("#topicModal").modal("show");
		}
	});
}

/**
 * This function populates topicModal in order to modify the topic.
 * @param data - from the ViewTopicServlet response
 */
function populateEditTopicModal(data) {
	document.getElementById("topic_id").value = data.id;
	document.getElementById("topic_title").value = data.title;
	document.getElementById("topic_description").value = data.description;
	document.getElementById("topic_domainId").value = data.domainId;
	var ul = document.getElementById("topic_keywords");
	ul.className = "list-inline";
	ul.innerHTML = ""; //clear the div
	for(var i=0; i<data.keywords.length; i++) {
		var li = document.createElement("li"); 
		li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + data.keywords[i].keyword + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeTopicKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
		li.setAttribute('data-value',data.keywords[i].keyword);
		li.setAttribute('data-id',data.keywords[i].id);
		ul.appendChild(li);
	}
	//modify modal title
	document.getElementById("topic-modal-title").innerHTML = "Edit Topic - "+data.title;
	//make all fields not read-only
	document.getElementById("topic_title").readOnly = false;
	document.getElementById("topic_description").readOnly = false;
	//display new keyword input field
	document.getElementById("topic_newkeyword").style.display = 'inline';
	//show submit button
	var submit = document.getElementById("topic-modal-submit");
	submit.style.display = "inline";
}

/**
 * This function saves the topic in database.
 * The domain can be new topic or an updated one.
 */
function saveTopic() {
	var id = document.getElementById("topic_id").value; //null when saving new domain
	var title = document.getElementById("topic_title").value;
	var description = document.getElementById("topic_description").value;
	var domainId = document.getElementById("topic_domainId").value;
	var domain_keywords = document.getElementById("topic_keywords").childNodes;
	var keywords_list = ' "keywords" : [';
	for(var i=0; i<domain_keywords.length; i++) {
		var keyword = domain_keywords[i].getAttribute("data-value");
		var keyword_id = domain_keywords[i].getAttribute("data-id");
		var keywordData = '{ "id" : '+keyword_id+', "keyword":"'+keyword+'"}';
		if(i!=domain_keywords.length-1) {
			keywords_list = keywords_list + keywordData + ", ";
		} else {
			keywords_list = keywords_list + keywordData;
		}		
	}
	keywords_list = keywords_list + "]";
	
	var topicData = '{ "id" : '+id+', "title":"'+title+'", "description":"'+description+'", "domainId":'+parseInt(domainId)+', '+keywords_list+'}';
	$.ajax({
		url: "SaveTopicServlet",
		type: 'POST',
		data: topicData,
		dataType: 'text',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			$('#topicModal').modal('hide');
			location.reload();
		}
	});
}

/**
 * This function populates the topicModal in order to save a new topic under the appropriate domain.
 * @param domainId
 * @param domainTitle
 */
function populateAddTopicModal(domainId, domainTitle) {
	//modify modal title
	document.getElementById("topic-modal-title").innerHTML = "Add new topic in "+domainTitle;
	//reset all form values - from editModal and viewModal actions
	document.getElementById("topic_id").value = 0;
	document.getElementById("topic_domainId").value = domainId;
	document.getElementById("topic_title").value = '';
	document.getElementById("topic_description").value = '';
	var ul = document.getElementById("topic_keywords");
	ul.innerHTML = ""; //clear the div
	//make all fields not read-only
	document.getElementById("topic_title").readOnly = false;
	document.getElementById("topic_description").readOnly = false;
	//display new keyword input field
	document.getElementById("topic_newkeyword").style.display = 'inline';
	//show submit button
	var submit = document.getElementById("topic-modal-submit");
	submit.style.display = "inline";
}

/**
 * For domains
 * This function adds the new keyword string into the list of all keywords.
 * On save, all the keywords in the list would be sent to the servlet.
 * @param newKeyword
 */
function addKeyword(newKeyword) {
	var ul = document.getElementById("domain_keywords");
	var li = document.createElement("li");
	li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + newKeyword.value + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
	li.setAttribute('data-id',0); //set new attribute as 0 so that it can be saved in database
	li.setAttribute('data-value',newKeyword.value);
	ul.appendChild(li);
	newKeyword.value = "";
}

/**
 * For domains
 * This function removes the list element on click of glyphicon-remove button.
 * @param keyword
 */
function removeKeyword(keyword) {
	keyword.parentNode.parentNode.parentNode.removeChild(keyword.parentNode.parentNode);
}

/**
 * For topics
 * This function adds the new keyword string into the list of all keywords.
 * On save, all the keywords in the list would be sent to the servlet.
 * @param newKeyword
 */
function addTopicKeyword(newKeyword) {
	var ul = document.getElementById("topic_keywords");
	var li = document.createElement("li");
	li.innerHTML = "<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default\">" + newKeyword.value + "</button><button type=\"button\" class=\"btn btn-danger\" onclick=\"removeTopicKeyword(this)\"><span class=\"glyphicon glyphicon-remove\"></span></button></div>";
	li.setAttribute('data-id',0); //set new attribute as 0 so that it can be saved in database
	li.setAttribute('data-value',newKeyword.value);
	ul.appendChild(li);
	newKeyword.value = "";
}

/**
 * For topics
 * This function removes the list element on click of glyphicon-remove button.
 * @param keyword
 */
function removeTopicKeyword(keyword) {
	keyword.parentNode.parentNode.parentNode.removeChild(keyword.parentNode.parentNode);
}