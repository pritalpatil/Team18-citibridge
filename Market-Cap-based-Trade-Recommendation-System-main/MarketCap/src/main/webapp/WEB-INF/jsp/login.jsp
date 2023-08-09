<html>
<head>
<title>Login Page</title>
<link href = "css/bootstrap.css" rel = "stylesheet" type = "text/css">
</head>

<body style="background-image:url('Stock Background1.jpg');">

<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #050A30;">
    
    <a class="navbar-brand" href="#" style="color:white">Stock Trade Recommender</a>
  </nav>
	
		<div class = "container" style="margin-top:70px">
			<div class = "row">
						
					<div class = "col-lg-3 col-md-3 col-sm-3 col-xs-12" ></div>					
						<div class = "col-lg-6 col-md-6 col-sm-6 col-xs-12">
								<div class = "jumbotron">
									<h1 class ="text-center">Login</h1>
									<br>
									
									 <form method ="post">
									 		<label class="control-lable" for="username">Username</label>
											<input type="text" name="userName" class = "form-control" placeholder="User Name"/>
											<br><br>
											<label class="control-lable" for="password">Passsword</label>
											<input type="password" name="password" class = "form-control"  placeholder="Password"/>
											<br><br>
											<div class="col-md-12 text-center">
												<button class = "btn btn-primary active" name="submit">Submit</button>
											</div>
											
									</form>
									<br>
									<p style="color:red; text-align:center;">${error}</p>
								</div>						
						</div>
		</div>
	</div>
	<script type = "text/javascript "  src = "js/bootstrap.js"></script>
	<script type = "text/javascript "  src = "js/jquery.js"></script>
</body>
</html>