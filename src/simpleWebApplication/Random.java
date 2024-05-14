package simpleWebApplication;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Random")
public class Random extends HttpServlet {
	public void service (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/invalidvalues.html");
		
		int min;
		int max;
		// Catch not a number
		try {
			min = Integer.parseInt(request.getParameter("a"));
			max = Integer.parseInt(request.getParameter("b"));
		} catch (NumberFormatException e) {
			dispatcher.forward(request, response);
			return;
		}
		
		// Catch wrong min max ratio
		if (min > max) {
			dispatcher.forward(request, response);
			return;
		}

		response.getWriter().println((int) ((Math.random() * (max - min)) + min));
	}
}
