/**
 * Send the updated user info to SaveUserServlet and save in database
 */
function saveUserInfo() {
	var username = document.getElementById("username").value;
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	var json = JSON.stringify({username:username, email:email, password:password});
	$.ajax({
		url: "SaveUserServlet",
		type: 'POST',
		data: json,
		dataType: 'text',
		contentType: "application/json",
		crossDomain: true,
		success: function(data) {
			if(data == 'success') {
				//display success alert
				document.getElementById("success-alert").style.display = 'block';
				document.getElementById("error-alert").style.display = 'none';
			} else {
				//display error alert
				document.getElementById("success-alert").style.display = 'none';
				document.getElementById("error-alert").style.display = 'block';
			}
		}, error: function(data) {
			//display error alert
			document.getElementById("success-alert").style.display = 'none';
			document.getElementById("error-alert").style.display = 'block';
		}
	});
}

/**
 * check if password and confirm_password field values match 
 */
function validatePassword() {
	
}