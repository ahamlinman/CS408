package tk.packattk;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("mytheme")
@Widgetset("tk.packattk.MyAppWidgetset")
public class SmokeTestUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(buildInterface());
    }

    protected AbstractLayout buildInterface() {
    	// Create vertical layout for components
    	final VerticalLayout layout = new VerticalLayout();

    	MenuBar mainMenu = new MenuBar();
    	mainMenu.setWidth("100%");
    	MenuItem mainItem = mainMenu.addItem("Packattk", null);
    	mainItem.addItem("About", null);

    	layout.addComponent(mainMenu);

    	return layout;
    }

    @WebServlet(urlPatterns = "/*", name = "SmokeTestUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = SmokeTestUI.class, productionMode = false)
    public static class SmokeTestUIServlet extends VaadinServlet {
    }
}
