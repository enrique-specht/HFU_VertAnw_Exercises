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
		
		<script>
			function refresh() {
				fetch("rest/kartenverkauf/sitzplaetze").then(response => {
					response.text().then(text => {
						if (response.ok) {
							const seats = JSON.parse(text);
							const grid = document.getElementById("seat-grid");
							grid.innerHTML = "";

							for (seat of seats) {
								let a = document.createElement('a');
								a.classList.add("grid-item");
								a.innerHTML = seat.sitznummer;
								
								if (seat.zustand == "FREI") {
									a.classList.add("free");
								} else if (seat.zustand == "RESERVIERT") {
									a.classList.add("reserved");
								} else if (seat.zustand == "VERKAUFT") {
									a.classList.add("sold");
								}
								
								grid.appendChild(a);
							}
						} else {
							alert("Konnte nicht geladen werden");
						}
					});
				});
				
				fetch("rest/kartenverkauf/reservierungen/status").then(response => {
					response.text().then(text => {
						if (response.ok) {
							const info = document.getElementById("reservation-info");
							info.innerHTML = "";
							info.innerHTML = `Reservierungen können \${text === "true" ? "noch" : "nicht mehr"} angenommen werden.`;
						} else {
							alert("Konnte nicht geladen werden");
						}
					});
				});
			}
			
			async function fetchRest(subdomain) {
				return await fetch("rest/kartenverkauf/" + subdomain, {method: "PUT"}).then(response => {
					return response.text().then(text => {
						return text;
					});
				});
			}
			
		</script>
	</head>
	<body class="p-2" onload="refresh()">
		<h1>Kartenverkauf</h1>
		<div class="d-flex">
			<div class="seats-wrapper">
				<div class="grid" id="seat-grid"></div>
				<div class="d-flex mt-2">
					<div class="flex-grow-1 free border border-dark text-center">frei</div>
					<div class="flex-grow-1 reserved border border-dark text-center">reserviert</div>
					<div class="flex-grow-1 sold border border-dark text-center">verkauft</div>
				</div>
				<span class="text-wrap" id="reservation-info"></span>
			</div>
			<div class="flex-grow-1 ms-4">
				<form action="" method="post" id="form">
					<div class="input-group">
						<div class="form-floating">
							<input type="number" id="seatnumber" name="seatnumber" class="form-control" placeholder="Sitznummer" aria-label="Sitznummer" required min="1" max="100"/>
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
				<script>
					document.getElementById("form").addEventListener("submit",async (e) => {
						e.preventDefault();

						const formData = new FormData(e.target);
						const formProps = Object.fromEntries(formData);
						const id = formProps.seatnumber;
						const name = formProps.name;
						const info = document.getElementById("success-info");
						let text;
						console.log(e.submitter.name)
						switch (e.submitter.name) {
						  case 'buy':
							  text = await fetchRest("kaufe/" + id);
							  break;
						  case 'reserve':
							  text = await fetchRest("reserviere/" + id + "/" + name);
							  break;
						  case 'buy_reservation':
							  text = await fetchRest("kaufe/" + id + "/" + name);
							  break;
						  case 'cancel':
							  text = await fetchRest("storniere/" + id);
							  break;
						  case 'remove_reservations':
							  text = await fetchRest("reservierungen/aufheben");
							  break;
						  case 'initialize':
							  text = await fetchRest("init");
							  break;
						}
						
						if(text) {
							info.innerHTML = text;
							setTimeout(() => {
								info.innerHTML = "";
							}, 5000)
						}
						
						refresh();
					});
				</script>
				<span class="mt-2 d-block" id="success-info"></span>
			</div>
		</div>
	</body>
</html>