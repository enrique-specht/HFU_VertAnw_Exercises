package webApplicationREST.datenbankanbindung;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.mysql.cj.jdbc.MysqlDataSource;

@Singleton
@Path("/kartenverkauf")
public class Kartenverkauf implements ExceptionMapper<Throwable> {
	
	public DataSource datasource;

	public Kartenverkauf() {
		MysqlDataSource mdatasource = new MysqlDataSource();
        mdatasource.setURL("jdbc:mysql://localhost:3306/kartenverkauf");
        mdatasource.setUser("admin");
        mdatasource.setPassword("passwort");
        this.datasource = mdatasource;
    }
	
	public static void main(String[] args) throws SQLException {
		//Kartenverkauf verkauf = new Kartenverkauf();
	}
	
	@Path("/sitzplatz/{nr}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Sitzplatz getSitzplatz(@PathParam("nr") int nr) throws SQLException {
		try (Connection connection = datasource.getConnection()) {
			ResultSet rs = connection.createStatement().executeQuery(String.format("SELECT * FROM sitzplaetze WHERE sitznummer = %s", nr));
			while (rs.next())
				return new Sitzplatz(rs.getInt("sitznummer"), Zustand.valueOf(rs.getString("zustand").toUpperCase()), rs.getString("reservierungsname"));
		}
		
		return null;
	}
	
	@Path("/sitzplaetze")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Sitzplatz[] getSitzplaetze() throws SQLException {
		List<Sitzplatz> list = new ArrayList<Sitzplatz>();
		
		try (Connection connection = datasource.getConnection()) {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM sitzplaetze");
			while (rs.next())
				list.add(new Sitzplatz(rs.getInt("sitznummer"), Zustand.valueOf(rs.getString("zustand").toUpperCase()), rs.getString("reservierungsname")));
		}
		
		System.out.println(list.toString());
		
		Sitzplatz[] sitzplatzArray = new Sitzplatz[ list.size() ];
		return list.toArray( sitzplatzArray );
	}
	
	@Path("/reservierungen/status")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public boolean getReservierungenAnnehmen() throws SQLException {
		try (Connection connection = datasource.getConnection()) {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM reservierungsannahme");
			while (rs.next())
				return rs.getBoolean("reservierungen-annehmen");
		}
		
		return false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("Reservierungsannahme: \n");
			sb.append(this.getReservierungenAnnehmen() + "\n\n");
			sb.append("Sitzplätze: \n");
			for(Sitzplatz sitz : this.getSitzplaetze()) {
				sb.append(sitz + "\n");
			}
			
		} catch (SQLException e) {
			throw new KartenverkaufException(e.getMessage());
		}
		
		return sb.toString();
	}
	
	@Path("/kaufe/{nr}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public void kaufe(@PathParam("nr") int nr) throws SQLException {
		Sitzplatz seat = this.getSitzplatz(nr);
		
		if (seat.zustand == Zustand.RESERVIERT || seat.zustand == Zustand.VERKAUFT) {
			throw new KartenverkaufException("Platz ist reserviert oder bereits verkauft!");
		}
		
		try (Connection connection = datasource.getConnection()) {
			connection.createStatement().executeUpdate(String.format("UPDATE sitzplaetze SET zustand = '%s' WHERE sitznummer = %s", Zustand.VERKAUFT.toString(), nr));
		}
		
		System.out.println("Sitzplatz erfolgreich gekauft!");
	}
	
	@Path("/reserviere/{nr}/{name}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public void reserviere(@PathParam("nr") int nr, @PathParam("name") String name) throws SQLException {
		if(name.equals("")) {
			throw new KartenverkaufException("Keine Name hinterlegt!");
		}
		
		if (!this.getReservierungenAnnehmen()) {
			throw new KartenverkaufException("Reservierungen werden derzeit nicht angenommen!");
		}
		
		Sitzplatz seat = this.getSitzplatz(nr);
		
		if (seat.zustand == Zustand.RESERVIERT || seat.zustand == Zustand.VERKAUFT) {
			throw new KartenverkaufException("Platz ist bereits reserviert oder bereits verkauft!");
		}
		
		try (Connection connection = datasource.getConnection()) {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			statement.executeUpdate(String.format("UPDATE sitzplaetze SET zustand = '%s' WHERE sitznummer = %s", Zustand.RESERVIERT.toString(), nr));
			statement.executeUpdate(String.format("UPDATE sitzplaetze SET reservierungsname = '%s' WHERE sitznummer = %s", name, nr));
			connection.commit();
			connection.setAutoCommit(true);
		}
		
		System.out.println("Sitzplatz erfolgreich reserviert!");
	}
	
	@Path("/kaufe/{nr}/{name}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public void kaufeReserviert(@PathParam("nr") int nr, @PathParam("name") String name) throws SQLException {
		Sitzplatz seat = this.getSitzplatz(nr);
		
		if (seat.zustand == Zustand.FREI || seat.zustand == Zustand.VERKAUFT) {
			throw new KartenverkaufException("Platz ist nicht reserviert!");
		}
		
		if (!seat.reservierungsname.toLowerCase().matches(name.toLowerCase())) {
			throw new KartenverkaufException("Platz ist nicht auf Sie reserviert!");
		}
		
		try (Connection connection = datasource.getConnection()) {
			connection.createStatement().executeUpdate(String.format("UPDATE sitzplaetze SET zustand = '%s' WHERE sitznummer = %s", Zustand.VERKAUFT.toString(), nr));
		}
		
		System.out.println("Reservierter Sitzplatz erfolgreich gekauft!");
	}
	
	@Path("/storniere/{nr}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public void storniere(@PathParam("nr") int nr) throws SQLException {
		try (Connection connection = datasource.getConnection()) {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			statement.executeUpdate(String.format("UPDATE sitzplaetze SET zustand = '%s' WHERE sitznummer = %s", Zustand.FREI.toString(), nr));
			statement.executeUpdate(String.format("UPDATE sitzplaetze SET reservierungsname = '' WHERE sitznummer = %s", nr));
			connection.commit();
			connection.setAutoCommit(true);
		}
		
		System.out.println("Sitzplatz storniert!");
	}
	
	@Path("/reservierungen/aufheben")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public void hebeReservierungenAuf() throws SQLException {
		Sitzplatz[] seats = this.getSitzplaetze();
		
		
		try (Connection connection = datasource.getConnection()) {
			connection.setAutoCommit(false);
			PreparedStatement pstatement1 = connection.prepareStatement(String.format("UPDATE sitzplaetze SET zustand = '%s' WHERE sitznummer = ?", Zustand.FREI.toString()));
			PreparedStatement pstatement2 = connection.prepareStatement("UPDATE sitzplaetze SET reservierungsname = '' WHERE sitznummer = ?");
			for (Sitzplatz seat: seats) {
				if (seat.zustand == Zustand.RESERVIERT) {
					pstatement1.setInt(1, seat.sitznummer);
					pstatement2.setInt(1, seat.sitznummer);
					pstatement1.execute();
					pstatement2.execute();
				}
			}
			connection.commit();
			connection.setAutoCommit(true);
		}
		
		try (Connection connection = datasource.getConnection()) {
			connection.createStatement().executeUpdate("UPDATE reservierungsannahme SET `reservierungen-annehmen` = '0'");
		}
		
		System.out.println("Reservierungen aufgehoben!");
	}
	
	@Path("/init")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public void initialisiere() throws SQLException {
		try (Connection connection = datasource.getConnection()) {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM sitzplaetze");
			
			PreparedStatement pstatement = connection.prepareStatement(String.format("INSERT INTO sitzplaetze (sitznummer, zustand, reservierungsname) VALUES (?, '%s', NULL)", Zustand.FREI.toString()));
			for (int i = 1; i <= 100; i++) {
				pstatement.setInt(1, i);
				pstatement.execute();
			}
			connection.commit();
			connection.setAutoCommit(true);
			connection.createStatement().executeUpdate("UPDATE reservierungsannahme SET `reservierungen-annehmen` = '1'");
		}
		
		System.out.println("Initialer Zustand hergestellt!");
	}
	
	@Override
	public Response toResponse(Throwable exception) {
		exception.printStackTrace();
		System.out.println("REST-Exception: " + exception.getMessage());
		return Response.status(500).entity(exception.getMessage()).type("text/plain").build();
	}
}
