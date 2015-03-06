package tk.packattk.components.listeners;

import tk.packattk.utils.Package;

import com.vaadin.data.util.BeanItem;

public interface PackageSelectListener {
	public void packageSelected(BeanItem<Package> selectedPackage);
}
