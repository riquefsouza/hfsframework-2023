package br.com.hfs.view.menu;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MenuCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String label;
    private List<MenuItem> menuItems;

    public MenuCategory(String label, List<MenuItem> menuItems) {
        this.label = label;
        this.menuItems = menuItems;
    }

    public String getLabel() {
        return label;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MenuCategory)) {
            return false;
        }
        MenuCategory other = (MenuCategory) obj;
        return Objects.equals(label, other.label);
    }

    @Override
    public String toString() {
        return "MenuCategory [label=" + label + "]";
    }
}
