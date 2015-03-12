package tk.packattk.ui;

import java.util.Collection;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.AdminScanOutAvailablePanel;
import tk.packattk.components.AdminScanOutRemovePanel;
import tk.packattk.utils.DatabaseWrappers;
import tk.packattk.utils.Package;
import tk.packattk.utils.Person;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
@Title("Packattk: Check Out Packages")
public class PackageCheckoutUI extends UI {

	private static final long serialVersionUID = 1L;
	private Person currentUser = null;

	@Override
	protected void init(VaadinRequest request) {
		currentUser = (Person) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("user");

		if(currentUser == null) {
			getUI().getPage().setLocation("/");
			return;
		}

		setContent(buildInterface());
	}

	protected AbstractLayout buildInterface() {
		// Create vertical layout for components
		final VerticalLayout layout = new VerticalLayout();

		final MenuBar mainMenu = new MenuBar();

		MenuItem mainItem = mainMenu.addItem("Packattk", null);
		mainItem.addItem("About", null);

		mainMenu.addItem("Administration", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/packages/admin");
			}
		});

		mainMenu.addItem("Enter", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/packages/enter");
			}
		});

		mainMenu.addItem("Log Out", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				VaadinService.getCurrentRequest().getWrappedSession().invalidate();
				getPage().setLocation("/");
			}
		});

		mainMenu.setWidth("100%");

		final HorizontalLayout scanLayout = new HorizontalLayout();

		final AdminScanOutAvailablePanel scanOutPanel = new AdminScanOutAvailablePanel();
		final AdminScanOutRemovePanel scanRemovePanel = new AdminScanOutRemovePanel(scanOutPanel);

		scanLayout.addComponent(scanOutPanel);
		scanLayout.addComponent(scanRemovePanel);
		scanLayout.setSizeFull();

		final HorizontalLayout buttonLayout = new HorizontalLayout();

		final Button submitButton = new Button("Submit");
		submitButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Collection<Package> removeList = scanRemovePanel.getPackagesToRemove();
				for(Package p : removeList) {
					if(!DatabaseWrappers.removePackage(p)) {
						Notification.show("Error checking out all packages");
						return;
					}
				}

				Notification.show(removeList.size() + " package(s) checked out");
				scanOutPanel.clear();
				scanRemovePanel.clear();
			}
		});

		final Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				scanOutPanel.clear();
				scanRemovePanel.clear();
			}
		});

		buttonLayout.addComponent(cancelButton);
		buttonLayout.addComponent(submitButton);

		layout.addComponent(mainMenu);
		layout.addComponent(scanLayout);
		layout.addComponent(buttonLayout);

		layout.setExpandRatio(scanLayout, 1.0f);
		layout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);

		layout.setSpacing(true);
		layout.setSizeFull();

		return layout;
	}

	@WebServlet(urlPatterns = "/packages/checkout/*", name = "PackageCheckoutUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = PackageCheckoutUI.class, productionMode = false)
	public static class PackageCheckoutUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

}
