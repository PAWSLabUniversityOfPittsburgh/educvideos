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
	//modify modal title
	document.getElementById("bookmarkTitle").innerHTML = "View Bookmark - "+data.videoTitle;
	//make all fields read-only
	document.getElementById("bookmarkNotes").readOnly = true;
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
	//modify modal title
	document.getElementById("bookmarkTitle").innerHTML = "Edit Bookmark - "+data.videoTitle;
	//make some fields read-only and others editable
	document.getElementById("bookmarkNotes").readOnly = false;
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
	
	var bookmarkData = '{ "id" : '+id+', "name":"'+name+'", "title":"'+title+'", "youtubeId":"'+youtubeId+'", "query":"'+query+'", "notes":"'+notes+'"}';
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

/***** UTILS *****/
/**
 * This ensures the youtube video stops playing on dismissing the modal
 */
$('#bookmarkModal').on('hidden.bs.modal', function () {
	document.getElementById("bookmarkIframe").src = "";
});