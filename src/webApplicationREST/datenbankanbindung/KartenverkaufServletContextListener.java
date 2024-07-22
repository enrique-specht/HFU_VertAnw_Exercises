package webApplicationREST.datenbankanbindung;

import java.sql.Connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class KartenverkaufServletContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce)  {
		try {
			Kartenverkauf kartenverkauf = new Kartenverkauf();
			try (Connection connection = kartenverkauf.datasource.getConnection()) {
				connection.prepareStatement("select * from `sitzplaetze`").executeQuery();	
				sce.getServletContext().setAttribute("kartenverkauf", new Kartenverkauf());			
				System.out.println("Initialisierung erfolgreich");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Initialisierung gescheitert");
			throw new RuntimeException("Initialisierung gescheitert",e);
		}
    }
}
