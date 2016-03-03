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
	//modify modal title
	document.getElementById("domain-modal-title").innerHTML = "View Domain";
	//make all fields read-only
	document.getElementById("domain_name").readOnly = true;
	document.getElementById("domain_title").readOnly = true;
	document.getElementById("domain_description").readOnly = true;
	document.getElementById("domain_author").readOnly = true;
	document.getElementById("domain_license").readOnly = true;
	document.getElementById("domain_version").readOnly = true;
	document.getElementById("domain_created").readOnly = true;
	document.getElementById("domain_modified").readOnly = true;
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
	//modify modal title
	document.getElementById("domain-modal-title").innerHTML = "Edit Domain";
	//make all fields read-only
	document.getElementById("domain_name").readOnly = true;
	document.getElementById("domain_title").readOnly = false;
	document.getElementById("domain_description").readOnly = false;
	document.getElementById("domain_author").readOnly = true;
	document.getElementById("domain_license").readOnly = false;
	document.getElementById("domain_version").readOnly = false;
	document.getElementById("domain_created").readOnly = true;
	document.getElementById("domain_modified").readOnly = true;
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
	
	var domainData = '{ "id" : '+id+', "name":"'+name+'", "title":"'+title+'", "description":"'+description+'", "author":"'+author+'", "license":"'+license+'", "version":"'+version+'", "created":"'+created+'", "modified":"'+modified+'"}';
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
	//make all fields are not read-only
	document.getElementById("domain_name").readOnly = false;
	document.getElementById("domain_title").readOnly = false;
	document.getElementById("domain_description").readOnly = false;
	document.getElementById("domain_author").readOnly = true;
	document.getElementById("domain_license").readOnly = false;
	document.getElementById("domain_version").readOnly = false;
	document.getElementById("domain_created").readOnly = true;
	document.getElementById("domain_modified").readOnly = true;
	//show submit button
	var submit = document.getElementById("domain-modal-submit");
	submit.style.display = "inline";
}





/*****Topic Related*****/

/**
 * Retrieves domain information from Servlet and displays them in read-mode in modal
 */
function viewTopic(topicId) {
	alert("hello");
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
	//modify modal title
	document.getElementById("topic-modal-title").innerHTML = "View Topic";
	//make all fields read-only
	document.getElementById("topic_title").readOnly = true;
	document.getElementById("domain_description").readOnly = true;
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
	//modify modal title
	document.getElementById("topic-modal-title").innerHTML = "Edit Topic";
	//make all fields not read-only
	document.getElementById("topic_title").readOnly = false;
	document.getElementById("topic_description").readOnly = false;
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
	
	var topicData = '{ "id" : '+id+', "title":"'+title+'", "description":"'+description+'", "domainId":'+domainId+'}';
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
	//make all fields not read-only
	document.getElementById("topic_title").readOnly = false;
	document.getElementById("topic_description").readOnly = false;
	//show submit button
	var submit = document.getElementById("topic-modal-submit");
	submit.style.display = "inline";
}