package tk.packattk.ui;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
@Title("Packattk: View Packages")
public class PackageListUI extends UI {

	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
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
				getPage().setLocation("/");
			}
		});

		mainMenu.setWidth("100%");

		final VerticalSplitPanel packagePanel = new VerticalSplitPanel();

		final Table packageTable = new Table("Current Packages");
		packageTable.addContainerProperty("Tracking Number", String.class, "");
		packageTable.addContainerProperty("Scanned At", Date.class, null);
		packageTable.addContainerProperty("Location", String.class, "");

		Item testRow = packageTable.getItem(packageTable.addItem());
		testRow.getItemProperty("Tracking Number").setValue("1Z555444333222111");
		testRow.getItemProperty("Scanned At").setValue(Calendar.getInstance().getTime());
		testRow.getItemProperty("Location").setValue("Shreve");

		packageTable.setSizeFull();
		packageTable.setSelectable(true);
		packageTable.setImmediate(true);
		packageTable.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Item i = packageTable.getItem(packageTable.getValue());
				if(i != null) {
					Notification.show(i.getItemProperty("Tracking Number").getValue().toString());
				}
			}
		});

		packagePanel.setFirstComponent(packageTable);
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
