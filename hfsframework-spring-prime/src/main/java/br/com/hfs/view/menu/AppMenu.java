package br.com.hfs.view.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.hfs.admin.model.AdmMenu;
import br.com.hfs.admin.service.AdmMenuService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Component
@ApplicationScoped
public class AppMenu {

	@Inject
	private AdmMenuService service;

    List<MenuCategory> menuCategoria;
    List<MenuItem> menuItems;

    @PostConstruct
    public void init() {
    	List<AdmMenu> lista = this.service.getRepository().findMenuRoot();
    	
        menuCategoria = new ArrayList<>();
        menuItems = new ArrayList<>();

        for (AdmMenu menu : lista) {
        
    		if (menu.getAdmSubMenus() != null) {
    			List<MenuItem> adminMenuItems = new ArrayList<>();
    			
    			for (AdmMenu subMenu : menu.getAdmSubMenus()) {
    				adminMenuItems.add(new MenuItem(subMenu.getDescription(), "private/" + subMenu.getUrl()));
    			}
    			
    			menuCategoria.add(new MenuCategory(menu.getDescription(), adminMenuItems));
    		}
        }
        /*
        //AJAX FRAMEWORK CATEGORY START
        List<MenuItem> adminMenuItems = new ArrayList<>();
        adminMenuItems.add(new MenuItem("Categoria dos Parâmetros", "private/admin/admParameterCategory/listAdmParameterCategory.xhtml"));
        adminMenuItems.add(new MenuItem("Parâmetros", "private/admin/admParameter/listAdmParameter.xhtml"));        
        adminMenuItems.add(new MenuItem("Perfis", "private/admin/admProfile/listAdmProfile.xhtml"));
        adminMenuItems.add(new MenuItem("Páginas", "private/admin/admPage/listAdmPage.xhtml"));
        adminMenuItems.add(new MenuItem("Menus", "private/admin/admMenu/listAdmMenu.xhtml"));
        adminMenuItems.add(new MenuItem("Usuários", "private/admin/admUser/listAdmUser.xhtml"));
        adminMenuItems.add(new MenuItem("Alterar senha", "private/admin/changePassword/editChangePassword.xhtml"));
        menuCategoria.add(new MenuCategory("Administrativo", adminMenuItems));
        
        List<MenuItem> sistemaMenuItems = new ArrayList<>();
        sistemaMenuItems.add(new MenuItem("Usuário logado", "system/usuario.xhtml"));
        sistemaMenuItems.add(new MenuItem("Configuração de Tela", "system/config.xhtml"));
        menuCategoria.add(new MenuCategory("Sistema", sistemaMenuItems));
        //AJAX FRAMEWORK CATEGORY END
		*/
        
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