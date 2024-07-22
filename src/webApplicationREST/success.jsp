<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		
		<!-- Bootstrap CSS -->
		<link href="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"
			rel="stylesheet"
			integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
			crossorigin="anonymous">
		<!-- Bootstrap Font Icon CSS -->
		<link rel="stylesheet"
			href="/webjars/bootstrap-icons/1.7.0/font/bootstrap-icons.css">
		<link rel="stylesheet" href="/style.css">
		<!-- Bootstrap Bundle with Popper -->
		<script src="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"
			integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
			crossorigin="anonymous"></script>
			
		<title>Bestätigung</title>
	</head>
	<body class="p-2">
		<h1>Bestätigung</h1>
		<p>Operation '${operation}' erfolgreich ausgeführt!</p>
		<a href="/index.jsp">zurück</a>
	</body>
</html>