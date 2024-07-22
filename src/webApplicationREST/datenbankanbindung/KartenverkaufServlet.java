package webApplicationREST.datenbankanbindung;

import java.io.IOException;
import java.sql.SQLException;

//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/kartenverkauf")
public class KartenverkaufServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Kartenverkauf k = new Kartenverkauf();
		//RequestDispatcher successDispatcher = request.getRequestDispatcher("/success.jsp");
		//RequestDispatcher errorDispatcher = request.getRequestDispatcher("/error.jsp");

		int seatId = request.getParameter("seatnumber") != null ? Integer.parseInt(request.getParameter("seatnumber")) : -1;
		String name = request.getParameter("name");
		
		try {
			if (request.getParameter("buy") != null) {
				request.getSession().setAttribute("operation", "kaufen");
				k.kaufe(seatId);
			} else if (request.getParameter("reserve") != null) {
				request.getSession().setAttribute("operation", "reservieren");
			    k.reserviere(seatId, name);
			} else if (request.getParameter("buy_reservation") != null) {
				request.getSession().setAttribute("operation", "kaufe reservierte");
				k.kaufeReserviert(seatId, name);
			} else if (request.getParameter("cancel") != null) {
				request.getSession().setAttribute("operation", "stornieren");
				k.storniere(seatId);
			} else if (request.getParameter("remove_reservations") != null) {
				request.getSession().setAttribute("operation", "Reservierungen entfernen");
			    k.hebeReservierungenAuf();
			} else if (request.getParameter("initialize") != null) {
				request.getSession().setAttribute("operation", "initialisieren");
			    k.initialisiere();
			}
			
			//successDispatcher.forward(request, response);
		} catch (KartenverkaufException e) {
			request.getSession().setAttribute("exception", e.getMessage());
			//errorDispatcher.forward(request, response);
		} catch (SQLException e) {
			request.getSession().setAttribute("exception", e.getMessage());
			//errorDispatcher.forward(request, response);
		} finally {
			response.sendRedirect("/index.jsp");
		}
	}
}
