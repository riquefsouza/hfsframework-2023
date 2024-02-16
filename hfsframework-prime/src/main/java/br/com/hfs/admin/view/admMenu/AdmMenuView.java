package br.com.hfs.admin.view.admMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.com.hfs.admin.model.AdmMenu;
import br.com.hfs.admin.model.AdmPage;
import br.com.hfs.admin.service.AdmMenuService;
import br.com.hfs.admin.service.AdmPageService;
import br.com.hfs.util.interceptors.HandlingExpectedErrors;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
@HandlingExpectedErrors
public class AdmMenuView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AdmMenuService service;
	
	@Inject
	private AdmPageService admPaginaService;

	private AdmMenu bean;
	
	private TreeNode<AdmMenu> menuRaiz;

	private TreeNode<AdmMenu> selecionadoMenu;
	
	private List<AdmPage> listaAdmPagina;

	private List<AdmMenu> listaAdmMenuParent;

	@PostConstruct
	public void init() {		
		this.listaAdmPagina = admPaginaService.findAll();
		initListaMenusParent();
		updateTreeMenus();
	}
	
	private void initListaMenusParent() {
		this.listaAdmMenuParent = new ArrayList<AdmMenu>();
		List<AdmMenu> lista = this.service.findAll();
		for (AdmMenu menu : lista) {
			if ((menu.getAdmSubMenus() != null) && (menu.getAdmPage() == null)) {
				this.listaAdmMenuParent.add(menu);
			}
		}
	}
	
	private boolean isMenuSelecionado() {
		return this.selecionadoMenu!=null;
	}
	
	public void onInsert() {
		this.bean = new AdmMenu();		
	}
	
	public void onEdit() {
		if (isMenuSelecionado()) {
			this.bean = selecionadoMenu.getData();
			PrimeFaces.current().executeScript("PF('editDialog').show()");
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selecione um item do Menu!"));
		
	}
	
	public void onDelete() {
		if (isMenuSelecionado()) {
			this.bean = selecionadoMenu.getData();
			PrimeFaces.current().executeScript("PF('excluirDialog').show()");
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selecione um item do Menu!"));
	}

	public void excluir() {
		this.service.deleteMenu(this.bean);
		this.bean = null;
		initListaMenusParent();
		updateTreeMenus();	
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Menu Excluído"));
		//PrimeFaces.current().ajax().update("formAdmMenu:messages");
	}

	public void salvar() {
		if (this.bean.getAdmMenuParent()!=null) {
			this.bean.setIdMenuParent(this.bean.getAdmMenuParent().getId());	
		}
		if (this.bean.getAdmPage()!=null) {
			this.bean.setIdPage(this.bean.getAdmPage().getId());
		}
		
		if (this.bean.getId() == null) {
			this.service.insert(this.bean);
			initListaMenusParent();
			updateTreeMenus();	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Menu Adicionado"));
		} else {
			this.service.update(this.bean);
			initListaMenusParent();
			updateTreeMenus();	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Menu Atualizado"));
		}
		
		PrimeFaces.current().executeScript("PF('editDialog').hide()");
		//PrimeFaces.current().ajax().update("formAdmMenu:messages");
	}

	public void updateTreeMenus() {
		List<AdmMenu> listaMenus = this.service.getRepository().findMenuRoot();
		
		AdmMenu menu = new AdmMenu();
		menu.setDescription("Menu do sistema");
		this.menuRaiz = new DefaultTreeNode<AdmMenu>(menu, null);
		this.menuRaiz.setExpanded(true);
		for (AdmMenu itemMenu : listaMenus) {
			TreeNode<AdmMenu> m = new DefaultTreeNode<AdmMenu>(itemMenu, this.menuRaiz);
			m.setExpanded(true);
			mountSubMenu(itemMenu, m);
		}
	}

	private List<TreeNode<AdmMenu>> mountSubMenu(AdmMenu menu, TreeNode<AdmMenu> pMenu) {
		List<TreeNode<AdmMenu>> lstSubMenu = new ArrayList<TreeNode<AdmMenu>>();
		if (menu.getAdmSubMenus() != null) {
			for (AdmMenu subMenu : menu.getAdmSubMenus()) {
				if (subMenu.isSubMenu()) {
					TreeNode<AdmMenu> m = new DefaultTreeNode<AdmMenu>(subMenu, pMenu);
					m.setExpanded(true);
					mountSubMenu(subMenu, m);
				} else {
					new DefaultTreeNode<AdmMenu>(subMenu, pMenu);
				}
			}
		}
		return lstSubMenu;
	}
	
	public void expandAll() {
		this.menuRaiz.getChildren().forEach(node -> {
			this.expandRecursive(node, true);
		});
	}

	public void collapseAll() {
		this.menuRaiz.getChildren().forEach(node -> {
			this.expandRecursive(node, false);
		});
	} 

	private void expandRecursive(TreeNode<AdmMenu> node, boolean isExpand) {
		node.setExpanded(isExpand);
		if (node.getChildCount() > 0) {
			node.getChildren().forEach(childNode -> {
				this.expandRecursive(childNode, isExpand);
			});
		}
	}	
	
	public AdmMenu getBean() {
		return bean;
	}

	public void setBean(AdmMenu bean) {
		this.bean = bean;
	}
	
	public TreeNode<AdmMenu> getMenuRaiz() {
		return menuRaiz;
	}

	public void setMenuRaiz(TreeNode<AdmMenu> menuRaiz) {
		this.menuRaiz = menuRaiz;
	}

	public TreeNode<AdmMenu> getSelecionadoMenu() {
		return selecionadoMenu;
	}

	public void setSelecionadoMenu(TreeNode<AdmMenu> selecionadoMenu) {
		this.selecionadoMenu = selecionadoMenu;
	}

	public List<AdmPage> getListaAdmPagina() {
		return listaAdmPagina;
	}

	public void setListaAdmPagina(List<AdmPage> listaAdmPagina) {
		this.listaAdmPagina = listaAdmPagina;
	}

	public List<AdmMenu> getListaAdmMenuParent() {
		return listaAdmMenuParent;
	}

	public void setListaAdmMenuParent(List<AdmMenu> listaAdmMenuParent) {
		this.listaAdmMenuParent = listaAdmMenuParent;
	}

}
