package tk.packattk.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.VaadinServlet;

@WebServlet(urlPatterns = "/api/*", name = "APIServlet")
public class APIProxyServlet extends VaadinServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String command = inputStreamToString(request.getInputStream());

		APIServer server = new APIServer();
		String reply = server.executeRequest(command);

		response.getWriter().print(reply);
	}

	// Based on http://stackoverflow.com/a/5445161
	private String inputStreamToString(InputStream is) {
		Scanner scanner = new Scanner(is);
		Scanner tokenizer = scanner.useDelimiter("\\A");
		String str = tokenizer.hasNext() ? tokenizer.next() : "";
		scanner.close();
		return str;
	}

}
