package tk.packattk.ui;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.PackageDetailDisplay;
import tk.packattk.utils.DatabaseWrappers;
import tk.packattk.utils.Package;
import tk.packattk.utils.Person;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
@Title("Packattk: View Packages")
public class PackageListUI extends UI {

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

		mainMenu.addItem("Log Out", new Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				VaadinService.getCurrentRequest().getWrappedSession().invalidate();
				getPage().setLocation("/");
			}
		});

		mainMenu.setWidth("100%");

		final VerticalSplitPanel packagePanel = new VerticalSplitPanel();

		final PackageDetailDisplay detailDisplay = new PackageDetailDisplay();

		final Table packageTable = new Table("Current Packages");
		BeanItemContainer<Package> packageContainer = new BeanItemContainer<Package>(Package.class);
		List<Package> packages = DatabaseWrappers.getPackages(currentUser);
		if (packages != null) {
			packageContainer.addAll(packages);
		} else {
			packageTable.setVisible(false);
		}

		packageTable.setContainerDataSource(packageContainer);
		packageTable.setVisibleColumns("tracking", "location", "name");
		packageTable.setColumnHeaders("Tracking #", "Location", "Name");

		packageTable.setSizeFull();
		packageTable.setSelectable(true);
		packageTable.setImmediate(true);
		packageTable.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Package p = (Package) packageTable.getValue();
				if(p != null) {
					detailDisplay.displayPackage(p);
				} else {
					detailDisplay.clearDisplay();
				}
			}
		});

		detailDisplay.setSizeFull();

		packagePanel.setFirstComponent(packageTable);
		packagePanel.setSecondComponent(detailDisplay);
		packagePanel.setSplitPosition(50, Sizeable.UNITS_PERCENTAGE);
		packagePanel.setSizeFull();

		layout.addComponent(mainMenu);
		layout.addComponent(packagePanel);
		layout.setExpandRatio(packagePanel, 1.0f);

		layout.setSpacing(true);
		layout.setSizeFull();

		return layout;
	}

	@WebServlet(urlPatterns = "/packages/view/*", name = "PackageListUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = PackageListUI.class, productionMode = false)
	public static class PackageListUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

}
