package tk.packattk;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.LoginWindow;
import tk.packattk.utils.DatabaseWrappers;
import tk.packattk.utils.Person;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
@Title("Packattk: Log In")
public class LoginUI extends UI {

	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		Person currentUser = (Person) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("user");
		if(currentUser != null) {
			redirectUser(currentUser);
		}
		
		setContent(buildInterface());
	}

	protected AbstractLayout buildInterface() {
		// Create vertical layout for components
		final VerticalLayout layout = new VerticalLayout();

		final MenuBar mainMenu = new MenuBar();

		MenuItem mainItem = mainMenu.addItem("Packattk", null);
		mainItem.addItem("About", null);

		mainMenu.addItem("Create Account", new Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/account/create");
			}
		});

		mainMenu.setWidth("100%");

		final LoginWindow login = new LoginWindow();
		login.setWidth("50%");
		login.setHeight("300px");

		layout.addComponent(mainMenu);
		layout.addComponent(login);

		layout.setSpacing(true);
		layout.setComponentAlignment(login, Alignment.MIDDLE_CENTER);

		login.addLoginListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String username = login.getUsername();
				String password = login.getPassword();

				try {
					login.getUsernameField().validate();
					login.getPasswordField().validate();
				} catch (InvalidValueException e) {
					Notification.show(e.getMessage().length() == 0 ? "There was an error" : e.getMessage());
					return;
				}

				try {
					if(DatabaseWrappers.checkLogin(username, password)) {
						Person currentUser = DatabaseWrappers.getPersonByUsername(username);
						VaadinService.getCurrentRequest().getWrappedSession().invalidate();
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute("user", currentUser);
						redirectUser(currentUser);
					} else {
						Notification.show("Incorrect username/password. Please try again.");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					Notification.show("An error occurred. Please try again.");
				}
			}
		});

		return layout;
	}
	
	private void redirectUser(Person currentUser) {
		if(currentUser.getIsAdmin()) {
			getUI().getPage().setLocation("/packages/admin");
		} else {
			getUI().getPage().setLocation("/packages/view");
		}
	}

	@WebServlet(urlPatterns = "/*", name = "LoginUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = LoginUI.class, productionMode = false)
	public static class LoginUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}
}
