package tk.packattk.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.VaadinServlet;

@WebServlet(urlPatterns = "/api/login/*", name = "LoginServlet")
public class LoginServlet extends VaadinServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String loginString = inputStreamToString(request.getInputStream());
		response.getWriter().println(loginString);
	}

	// http://stackoverflow.com/a/5445161
	private String inputStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
