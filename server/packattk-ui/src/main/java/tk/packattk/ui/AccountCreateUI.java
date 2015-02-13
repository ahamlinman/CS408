package tk.packattk.ui;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.AccountCreateWindow;
import tk.packattk.components.LoginWindow;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
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
@Title("Packattk: Create Account")
public class AccountCreateUI extends UI {

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setContent(buildInterface());
	}

	protected AbstractLayout buildInterface() {
		// Create vertical layout for components
		final VerticalLayout layout = new VerticalLayout();

		final MenuBar mainMenu = new MenuBar();

		MenuItem mainItem = mainMenu.addItem("Packattk", null);
		mainItem.addItem("About", null);

		mainMenu.addItem("Log In", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/");
			}
		});

		mainMenu.setWidth("100%");

		final AccountCreateWindow create = new AccountCreateWindow();
		create.setWidth("50%");
		create.setHeight("300px");

		layout.addComponent(mainMenu);
		layout.addComponent(create);

		layout.setSpacing(true);
		layout.setComponentAlignment(create, Alignment.MIDDLE_CENTER);

		create.addCreateListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String email = create.getEmail();
				String password = create.getPassword();
				boolean passwordsEqual = create.passwordsEqual();
			}
		});

		return layout;
	}

	@WebServlet(urlPatterns = "/account/create/*", name = "AccountCreateUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = AccountCreateUI.class, productionMode = false)
	public static class AccountCreateUIServlet extends VaadinServlet {
	}
}
