package tk.packattk;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.LoginWindow;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
public class LoginUI extends UI {

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setContent(buildInterface());
	}

	protected AbstractLayout buildInterface() {
		// Create vertical layout for components
		final VerticalLayout layout = new VerticalLayout();

		final MenuBar mainMenu = new MenuBar();
		mainMenu.setWidth("100%");
		MenuItem mainItem = mainMenu.addItem("Packattk", null);
		mainItem.addItem("About", null);

		final LoginWindow login = new LoginWindow();
		login.setWidth("50%");
		login.setHeight("300px");

		layout.addComponent(mainMenu);
		layout.addComponent(login);

		layout.setSpacing(true);
		layout.setComponentAlignment(login, Alignment.MIDDLE_CENTER);

		login.addLoginListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String username = login.getUsername();
				String password = login.getPassword();

				Notification.show(username + ":" + password);
			}
		});

		return layout;
	}

	@WebServlet(urlPatterns = "/*", name = "LoginUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = LoginUI.class, productionMode = false)
	public static class LoginUIServlet extends VaadinServlet {
	}
}
