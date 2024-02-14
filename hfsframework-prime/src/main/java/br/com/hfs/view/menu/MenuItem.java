package br.com.hfs.view.menu;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String label;
    private String url;
    private List<MenuItem> menuItems;
    private String badge;
    private String parentLabel;

    public MenuItem(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public MenuItem(String label, String url, String badge) {
        this.label = label;
        this.url = url;
        this.badge = badge;
    }

    public MenuItem(String label, List<MenuItem> menuItems) {
        this.label = label;
        this.menuItems = menuItems;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public String getBadge() {
        return badge;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public String getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MenuItem)) {
            return false;
        }
        MenuItem other = (MenuItem) obj;
        return Objects.equals(label, other.label) && Objects.equals(url, other.url);
    }

    @Override
    public String toString() {
        return "MenuItem [label=" + label + ", url=" + url + "]";
    }
}
