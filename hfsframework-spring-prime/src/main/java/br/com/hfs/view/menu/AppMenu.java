package br.com.hfs.view.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@Component
@ApplicationScoped
public class AppMenu {

    List<MenuCategory> menuCategoria;
    List<MenuItem> menuItems;

    @PostConstruct
    public void init() {
        menuCategoria = new ArrayList<>();
        menuItems = new ArrayList<>();

        //AJAX FRAMEWORK CATEGORY START
        List<MenuItem> adminMenuItems = new ArrayList<>();
        adminMenuItems.add(new MenuItem("Categoria dos Parâmetros", "private/admin/admParameterCategory/listAdmParameterCategory.faces"));
        menuCategoria.add(new MenuCategory("Administrativo", adminMenuItems));
        
        List<MenuItem> sistemaMenuItems = new ArrayList<>();
        sistemaMenuItems.add(new MenuItem("Usuário logado", "private/system/usuario.faces"));
        sistemaMenuItems.add(new MenuItem("Configuração de Tela", "private/system/config.faces"));
        menuCategoria.add(new MenuCategory("Sistema", sistemaMenuItems));
        //AJAX FRAMEWORK CATEGORY END

        for (MenuCategory category : menuCategoria) {
            for (MenuItem menuItem : category.getMenuItems()) {
                menuItem.setParentLabel(category.getLabel());
                if (menuItem.getUrl() != null) {
                    menuItems.add(menuItem);
                }
                if (menuItem.getMenuItems() != null) {
                    for (MenuItem item : menuItem.getMenuItems()) {
                        item.setParentLabel(menuItem.getLabel());
                        if (item.getUrl() != null) {
                            menuItems.add(item);
                        }
                    }
                }
            }
        }
    }

    public List<MenuItem> completeMenuItem(String query) {
        String queryLowerCase = query.toLowerCase();
        List<MenuItem> filteredItems = new ArrayList<>();
        for (MenuItem item : menuItems) {
            if (item.getUrl() != null && (item.getLabel().toLowerCase().contains(queryLowerCase) ||
                        item.getParentLabel().toLowerCase().contains(queryLowerCase))) {
                filteredItems.add(item);
            }
            if (item.getBadge() != null) {
                if (item.getBadge().toLowerCase().contains(queryLowerCase)) {
                    filteredItems.add(item);
                }
            }
        }
        filteredItems.sort(Comparator.comparing(MenuItem::getParentLabel));
        return filteredItems;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public List<MenuCategory> getMenuCategoria() {
        return menuCategoria;
    }
}