<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="datenbankanbindung.*"%>
<%
	Kartenverkauf k = (Kartenverkauf)application.getAttribute("kartenverkauf");
	Sitzplatz[] seats = k.getSitzplaetze();
	boolean allowReservations = k.getReservierungenAnnehmen();
%>
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
		
		<title>Kartenverkauf</title>
		
		<style>
			.seats-wrapper {
				max-width: min-content
			}
			
			.grid {
				display: grid;
				grid-template-columns: repeat(10, 2rem);
				gap: 0.25rem;
			}
			
			.grid-item {
				border: 1px solid black;
				text-align: center;
				text-decoration: none;
				color: black;
			}
			
			.grid-item:hover {
				//font-weight: bold;
				color: inherit;
				//cursor: pointer;
			}
			
			.free {
				background-color: #91d3c1;
			}
			
			.reserved {
				background-color: #45ba86;
			}
			
			.sold {
				background-color: #3e7fb1;
			}
			
			.form-control {
				min-width: 15rem;
			}
		</style>
	</head>
	<body class="p-2">
		<h1>Kartenverkauf</h1>
		<div class="d-flex">
			<div class="seats-wrapper">
				<div class="grid">
					<%for(Sitzplatz seat : seats) {%>
						<a
							class="grid-item
							<%=seat.zustand.equals(Zustand.FREI) ? "free" : "" %>
							<%=seat.zustand.equals(Zustand.RESERVIERT) ? "reserved" : "" %>
							<%=seat.zustand.equals(Zustand.VERKAUFT) ? "sold" : "" %>
							">
							<%=seat.sitznummer%>
						</a>
					<%}%>
				</div>
				<div class="d-flex mt-2">
					<div class="flex-grow-1 free border border-dark text-center">frei</div>
					<div class="flex-grow-1 reserved border border-dark text-center">reserviert</div>
					<div class="flex-grow-1 sold border border-dark text-center">verkauft</div>
				</div>
				<span class="text-wrap">
					Reservierungen können
					<%= allowReservations ? "noch" : "nicht mehr" %>
					angenommen werden.
				</span>
			</div>
			<div class="flex-grow-1 ms-4">
				<form action="kartenverkauf" method="post">
					<div class="input-group">
						<div class="form-floating">
							<input type="number" id="seatnumber" name="seatnumber" class="form-control" placeholder="Sitznummer" aria-label="Sitznummer" required min="1" max="<%= seats.length %>"/>
							<label for="seatnumber">Sitznummer</label>
						</div>
						<div class="form-floating ms-2">
							<input type="text" id="name" name="name" class="form-control" placeholder="Name" aria-label="Name"/>
							<label for="name">Name</label>
						</div>				
					</div>
					<div class="mt-2">
						<input type="submit" name="buy" value="Kaufen"/>
						<input type="submit" name="reserve" value="Reservieren"/>
						<input type="submit" name="buy_reservation" value="Reservierung kaufen"/>
						<input type="submit" name="cancel" value="Stornieren"/>
					</div>
				</form>
				<form action="kartenverkauf" method="post">
					<div class="mt-2">
						<input type="submit" name="remove_reservations" value="Alle Reservierungen aufheben"/>
						<input type="submit" name="initialize" value="Initialisieren"/>
					</div>
				</form>
				<span>
					
					${exception != null && operation != null ? "Operation geschreitert: " += exception : ""}
					${exception == null && operation != null ? "Operation erfolgreich" : ""}
					<% 
						session.removeAttribute("exception");
						session.removeAttribute("operation");
					%>
				</span>
			</div>
		</div>
	</body>
</html>