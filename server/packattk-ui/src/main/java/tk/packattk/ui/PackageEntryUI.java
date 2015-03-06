package tk.packattk.ui;

import java.util.Collection;

import javax.servlet.annotation.WebServlet;

import tk.packattk.components.AdminPersonPanel;
import tk.packattk.components.AdminScanPanel;
import tk.packattk.components.listeners.PackageSelectListener;
import tk.packattk.utils.DatabaseWrappers;
import tk.packattk.utils.Package;
import tk.packattk.utils.Person;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
@Title("Packattk: Enter Packages")
public class PackageEntryUI extends UI {

	private static final long serialVersionUID = 1L;

	private static FieldGroup currentBinder = null;
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

		final HorizontalLayout mainLayout = new HorizontalLayout();

		final AdminScanPanel scanPanel = new AdminScanPanel();
		final FormLayout detailForm = new FormLayout();
		final AdminPersonPanel personPanel = new AdminPersonPanel();

		final TextField trackingNumberField = new TextField("Tracking #");
		trackingNumberField.setEnabled(false);
		detailForm.addComponent(trackingNumberField);

		final TextField locationField = new TextField("Location");
		locationField.setImmediate(true);
		detailForm.addComponent(locationField);

		final TextField nameField = new TextField("Name");
		locationField.setImmediate(true);
		detailForm.addComponent(nameField);

		final Button deleteButton = new Button("Delete");
		deleteButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				BeanItem<Package> currentPackage = (BeanItem<Package>) currentBinder.getItemDataSource();
				currentBinder.unbind(trackingNumberField);
				currentBinder.unbind(locationField);
				currentBinder.unbind(nameField);
				trackingNumberField.setValue("");
				locationField.setValue("");
				nameField.setValue("");
				deleteButton.setEnabled(false);
				currentBinder = null;
				scanPanel.removePackage(currentPackage);
			}
		});
		deleteButton.setEnabled(false);
		detailForm.addComponent(deleteButton);

		scanPanel.setPackageSelectListener(new PackageSelectListener() {
			@Override
			public void packageSelected(BeanItem<Package> selectedPackage) {
				if(selectedPackage != null) {
					currentBinder = new FieldGroup(selectedPackage);
					currentBinder.setBuffered(false);
					currentBinder.bind(trackingNumberField, "tracking");
					trackingNumberField.setEnabled(false);
					currentBinder.bind(locationField, "location");
					currentBinder.bind(nameField, "name");
					deleteButton.setEnabled(true);
				} else {
					try {
						currentBinder.commit();
					} catch (CommitException e) {
						e.printStackTrace();
						Notification.show("Unable to set location");
					}
					currentBinder.unbind(trackingNumberField);
					currentBinder.unbind(locationField);
					currentBinder.unbind(nameField);
					trackingNumberField.setValue("");
					locationField.setValue("");
					nameField.setValue("");
					deleteButton.setEnabled(false);
					currentBinder = null;
				}
			}
		});

		mainLayout.addComponent(scanPanel);
		mainLayout.addComponent(detailForm);
		mainLayout.addComponent(personPanel);

		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(false, true, false, true));
		mainLayout.setSizeFull();

		final Button submitButton = new Button("Submit");
		submitButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Collection<Package> packages = scanPanel.getPackages();
				Person owner = personPanel.getPerson();

				if(packages.isEmpty()) {
					Notification.show("Please scan packages first");
					return;
				}

				if(owner == null) {
					Notification.show("Please select an owner first");
					return;
				}

				for(Package p : packages) {
					p.setStudent(owner);
					p.setAdmin(owner);
					p.setDestination(owner.getLocation());
					if(!DatabaseWrappers.addPackage(p)) {
						Notification.show("Error adding packages");
						return;
					}
				}

				scanPanel.clear();
				personPanel.clear();

				if (currentBinder != null) {
					currentBinder.unbind(trackingNumberField);
					currentBinder.unbind(locationField);
					currentBinder.unbind(nameField);
					trackingNumberField.setValue("");
					locationField.setValue("");
					nameField.setValue("");
					deleteButton.setEnabled(false);
					currentBinder = null;
				}

				Notification.show(
						"Scanned " + packages.size() + " package(s) to "
								+ owner.getFirstName() + " " + owner.getLastName());

				scanPanel.focusScanField();
			}
		});

		layout.addComponent(mainMenu);
		layout.addComponent(mainLayout);
		layout.setExpandRatio(mainLayout, 1.0f);
		layout.addComponent(submitButton);
		layout.setComponentAlignment(submitButton, new Alignment(10));

		layout.setSpacing(true);
		layout.setSizeFull();

		return layout;
	}

	@WebServlet(urlPatterns = "/packages/enter/*", name = "PackageEntryUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = PackageEntryUI.class, productionMode = false)
	public static class PackageEntryUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

}
