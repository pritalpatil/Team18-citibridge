<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Select Market Cap</title>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<link href = "css/bootstrap.css" rel = "stylesheet" type = "text/css">
</head>
<body style="background-image:url('Stock Background.jpg'); background-size: cover;">

<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
%>
<div class="navbar" style="background-color: #050A30; overflow: hidden;">
<form method="post" style="width: 100%; margin-bottom:0px;">
<a class="navbar-brand" href="#" style="color:white">Stock Recommender</a>
	<button class="nav-item nav-link active pull-right" name="logout" value="logout" style="color:black; margin-left:5px; float: right; background-color: #d3d3d3; border-color: #474a5e; ">Logout</button>
	<button class="nav-item nav-link active pull-right" name="show" value="show" style="color:black; float: right; background-color: #d3d3d3; border-color: #474a5e; ">My Saved Stocks</button>
</form>
</div>




	<form method ="post">
		
		<br><br>
			<div class="row">
			  <div class="col-sm-4">
			    <div class="card">
			      <div class="card-body">
			        <h5 class="card-title text-center">Small Cap</h5>
			        <p class="card-text">Companies with a market cap (capitalization) or market value $2 billion or less.</p>
			        <div class="col-md-12 text-center">
			        <button name="type" value="small" class="btn btn-primary" style="background-color: #000C66;">Top 5 Stocks</button>
			        </div>
			      </div>
			    </div>
			  </div>
			  <div class="col-sm-4">
			    <div class="card">
			      <div class="card-body">
			        <h5 class="card-title text-center">Mid Cap</h5>
			        <p class="card-text">Companies with a market cap (capitalization) or market value between $2 and $10 billion.</p>
			        <div class="col-md-12 text-center">
			        <button name="type" value="mid" class="btn btn-primary" style="background-color: #000C66;">Top 5 Stocks</button></div>
			      </div>
			    </div>
			  </div>
			  <div class="col-sm-4">
			    <div class="card">
			      <div class="card-body">
			        <h5 class="card-title text-center">Large Cap</h5>
			        <p class="card-text">Companies with a market cap (capitalization) or market value $5 billion or more.</p>
			        <div class="col-md-12 text-center">
			        <button name="type" value="large" class="btn btn-primary" style="background-color: #000C66;">Top 5 Stocks</button></div>
			      </div>
			    </div>
			  </div>
	</div>
		<br><br>
		
		
		<table border="1" align="center" style="background-color:white; border-radius:5px">
		<h2 style="color:white; text-align:center">${notsaved}</h2>
			<c:forEach var="stk" items="${response}" varStatus="theCount">
			
			<c:if test="${theCount.count le 1}">
				<h2 style="color:white; text-align:center">Recommended Stocks:</h2>
				
 				<thead >
				<tr style="padding:15px">
					<th style="padding:15px">Sr.No.</th>
					<th style="padding:15px">Symbol</th>
					<th style="padding:15px">Price</th>
					<th style="padding:15px">Market Cap</th>
					<th style="padding:15px" name="quantity">Quantity</th>
					<th style="padding:15px">Save</th>
				</tr>
			</thead>
			</c:if>
				<tr style="padding:15px">
					<td style="padding:15px">${theCount.count}</td>
					<td style="padding:15px">${stk.symbol}</td>
					<td style="padding:15px">${stk.price}</td>
					<td style="padding:15px">${stk.marketCap}</td>
					<td style="padding:15px"><input type="number" name="quantity" min=1></td>
					<td><button name="ticker" value=${stk.symbol} onclick="save()">Save</button></td>

				<tr>
			</c:forEach>
		</table>
		
		
		<table border="1" align="center" style="background-color:white; border-radius:5px">
			<c:forEach var="stk" items="${showall}" varStatus="theCount">
			<c:if test="${theCount.count le 1}">
				<h2 style="color:white; text-align:center">Saved Stocks:<h2>
				<thead>
					<tr style="padding:15px">
						<th style="padding:15px">Sr.No.</th>
						<th style="padding:15px">Symbol</th>
						<th style="padding:15px">Price</th>
						<th style="padding:15px">Date</th>
						<th style="padding:15px">Time</th>
						<th style="padding:15px">Quantity</th>
						<th style="padding:15px">Unsave Stock</th>
					</tr>
				</thead>
			</c:if>
				<tr style="padding:15px">
					<td style="padding:15px">${theCount.count}</td>
					<td style="padding:15px">${stk.symbol}</td>
					<td style="padding:15px">${stk.price}</td>
					<td style="padding:15px">${stk.date}</td>
					<td style="padding:15px">${stk.time}</td>
					<td style="padding:15px">${stk.quantity}</td>
					<td style="padding:15px"><button name="unsave" value="${stk.symbol} ${stk.time}" onclick="remove()">Delete</button></td>
				<tr>
			</c:forEach>
		</table>
		<script type = "text/javascript "  src = "js/bootstrap.js"></script>
	<script type = "text/javascript "  src = "js/jquery.js"></script>
	<script>
		function save() {
			swal({
				  title: "Saved Successfully!!",
				  icon: "success",
				  button: "OK"
				});
		}
		
		function remove() {
			swal({
				  title: "Deleted Successfully!!",
				  icon: "success",
				  button: "OK"
				});
		}
	</script>
	
	</form>
</body>
</html>
