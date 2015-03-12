package tk.packattk.ui;

import java.util.Calendar;
import java.util.Collection;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.AdminPersonPanel;
import tk.packattk.components.listeners.PersonSelectListener;
import tk.packattk.utils.DatabaseWrappers;
import tk.packattk.utils.Package;
import tk.packattk.utils.Person;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
@Title("Packattk: Administration")
public class PackageAdminUI extends UI {

	private static final long serialVersionUID = 1L;
	private Person currentUser = null;
	private BeanItemContainer<Package> oldPackageContainer;

	@Override
	protected void init(VaadinRequest request) {
		currentUser = (Person) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("user");

		if(currentUser == null || !currentUser.getIsAdmin()) {
			getUI().getPage().setLocation("/");
			return;
		}

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -7);
		oldPackageContainer = new BeanItemContainer<Package>(Package.class);
		oldPackageContainer.addAll(DatabaseWrappers.getOldPackages(c.getTimeInMillis()));

		setContent(buildInterface());
	}

	protected AbstractLayout buildInterface() {
		// Create vertical layout for components
		final VerticalLayout layout = new VerticalLayout();

		final MenuBar mainMenu = new MenuBar();

		MenuItem mainItem = mainMenu.addItem("Packattk", null);
		mainItem.addItem("About", null);

		mainMenu.addItem("Enter", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/packages/enter");
			}
		});

		mainMenu.addItem("Check Out", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/packages/checkout");
			}
		});

		mainMenu.addItem("View My Packages", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getPage().setLocation("/packages/view");
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

		final HorizontalLayout tableLayout = new HorizontalLayout();

		final Table packageTable = new Table();
		packageTable.setContainerDataSource(oldPackageContainer);
		packageTable.setVisibleColumns("tracking", "location", "name");
		packageTable.setColumnHeaders("Tracking #", "Location", "Name");
		packageTable.setCaption("Packages older than one week");
		packageTable.setSizeFull();

		final AdminPersonPanel personTable = new AdminPersonPanel();
		personTable.setPersonSelectListener(new PersonSelectListener() {
			@Override
			public void personSelected(Person selectedPerson) {
				if(selectedPerson == null) {
					packageTable.setContainerDataSource(oldPackageContainer);
					packageTable.setVisibleColumns("tracking", "location", "name");
					packageTable.setColumnHeaders("Tracking #", "Location", "Name");
					packageTable.setCaption("Packages older than one week");
				} else {
					BeanItemContainer<Package> personPackages =
							new BeanItemContainer<Package>(Package.class);
					
					Collection<Package> pkgs = DatabaseWrappers.getPackages(selectedPerson);
					
					if(pkgs != null) {
						personPackages.addAll(pkgs);
						packageTable.setContainerDataSource(personPackages);
						packageTable.setVisibleColumns("tracking", "location", "name");
						packageTable.setColumnHeaders("Tracking #", "Location", "Name");
						packageTable.setCaption("Packages for " + selectedPerson.getFirstName()
								+ " " + selectedPerson.getLastName());
					} else {
						packageTable.setContainerDataSource(null);
						packageTable.setCaption("No packages for " + selectedPerson.getFirstName()
								+ " " + selectedPerson.getLastName());
					}
				}
			}
		});
		personTable.setSizeFull();

		tableLayout.addComponent(personTable);
		tableLayout.addComponent(packageTable);
		tableLayout.setSizeFull();

		layout.addComponent(mainMenu);
		layout.addComponent(tableLayout);
		layout.setExpandRatio(tableLayout, 1.0f);

		layout.setSpacing(true);
		layout.setSizeFull();

		return layout;
	}

	@WebServlet(urlPatterns = "/packages/admin/*", name = "PackageAdminUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = PackageAdminUI.class, productionMode = false)
	public static class PackageAdminUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

}
