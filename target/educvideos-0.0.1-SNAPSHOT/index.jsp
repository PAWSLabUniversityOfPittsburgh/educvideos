<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
</head>
<body>
	<div class="container">
		<div class="jumbotron text-center">
			<h1>Welcome to Video Authoring!</h1>
		</div>
		<div class="row">
			<div class="col-md-4 col-md-offset-2">
				<form action="LoginServlet" method="post" role="form" class="">
					<fieldset>
						<legend>Login</legend>
						<div class="form-group">
							<label for="uname">Username:</label><input id="login_username" type="text" name="username" placeholder="Username" required="required" class="form-control">
						</div>
						<div class="form-group">
							<label for="password">Password:</label><input id="login_password" type="password" name="password" placeholder="Password" required="required" class="form-control">
						</div>
						<input type="submit" value="Login" class="btn btn-info btn-lg"/>
						<div class="alert">
							<%
								try {
									String error = (String) request.getAttribute("error");
									if(error!=null)
										out.println(error);
								} catch (Exception e) {
									e.printStackTrace();
								}
							%>
						</div>
					</fieldset>
				</form>
			</div>
			<div class="col-md-4">
				<form action="SignupServlet" method="post" class="">
					<fieldset>
						<legend>New User? Create a New Account!</legend>
						<div class="form-group">
						<label for="uname">Username:</label><input type="text" name="username" placeholder="Username" required="required" class="form-control" id="username">
						</div>
						<div class="form-group">
						<label for="email">Email address:</label><input type="email" name="email" placeholder="Email address" required="required" class="form-control" id="email">
						</div>
						<div class="form-group">
						<label for="password">Password:</label><input type="password" name="password" id="password" placeholder="Password" required="required" onblur="validatePassword()" class="form-control">
						</div>
						<div class="form-group">
						<label for="confirm_password">Confirm password:</label><input type="password" name="confirm_password" id="confirm_password" placeholder="Confirm Password" required="required" onblur="validatePassword()" class="form-control">
						</div>
						
						<input type="submit" value="Sign Up" class="btn btn-info btn-lg"/>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>