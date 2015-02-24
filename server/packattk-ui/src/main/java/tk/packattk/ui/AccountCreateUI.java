package tk.packattk.ui;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.AccountCreateWindow;
import tk.packattk.utils.DatabaseWrappers;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Validator.InvalidValueException;
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

	private static final long serialVersionUID = 1L;

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
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/");
			}
		});

		mainMenu.setWidth("100%");

		final AccountCreateWindow create = new AccountCreateWindow();
		create.setWidth("50%");
		create.setHeight("500px");

		layout.addComponent(mainMenu);
		layout.addComponent(create);

		layout.setSpacing(true);
		layout.setComponentAlignment(create, Alignment.MIDDLE_CENTER);

		create.addCreateListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					create.validate();
				} catch (InvalidValueException e) {
					Notification.show(e.getMessage().length() == 0 ? "An error occurred" : e.getMessage());
					return;
				}

				if (!DatabaseWrappers.addPerson(create.getPerson())) {
					Notification.show("An error occurred and the account was not created.");
					return;
				}

				getUI().getPage().setLocation("/");
			}
		});

		return layout;
	}

	@WebServlet(urlPatterns = "/account/create/*", name = "AccountCreateUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = AccountCreateUI.class, productionMode = false)
	public static class AccountCreateUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}
}
