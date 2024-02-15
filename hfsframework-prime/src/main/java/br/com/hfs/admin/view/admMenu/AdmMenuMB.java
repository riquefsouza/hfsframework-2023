package br.com.hfs.admin.view.admMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeSelectEvent;
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
public class AdmMenuMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AdmMenuService service;
	
	@Inject
	private AdmPageService admPaginaService;

	private List<AdmMenu> listaBean;
	
	private AdmMenu bean;
	
	private AdmMenu selecionadoBean;

	private TreeNode<AdmMenu> menuRaiz;

	private TreeNode<AdmMenu> menuSelecionado;
	
	private List<AdmPage> listaAdmPagina;

	private List<AdmMenu> listaMenusParent;

	@PostConstruct
	public void init() {		
		this.listaAdmPagina = admPaginaService.findAll();
		this.listaBean = this.service.findAll();
		initListaMenusParent();
		updateTreeMenus();		
	}
	
	private void initListaMenusParent() {
		this.listaMenusParent = new ArrayList<AdmMenu>();
		List<AdmMenu> lista = this.service.findAll();
		for (AdmMenu menu : lista) {
			if ((menu.getAdmSubMenus() != null) && (menu.getAdmPage() == null)) {
				this.listaMenusParent.add(menu);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void listenerMenuSelecionado(NodeSelectEvent event) {
		this.menuSelecionado = event.getTreeNode();
	}

	public void onInsert() {
		this.bean = new AdmMenu();
	}
	
	public void onEdit(AdmMenu item) {
		this.bean = item;
	}
	
	public void onDelete(AdmMenu item) {
		this.bean = item;
	}

	public void excluir() {
		this.service.delete(this.bean);
		this.listaBean.remove(this.bean);
		this.bean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Menu Excluído"));
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
	}

	public void salvar() {
		if (this.bean.getId() == null) {
			this.listaBean.add(this.service.insert(this.bean));
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Menu Adicionado"));
		} else {
			OptionalInt indice = IntStream
		      .range(0, listaBean.size())
		      .filter(i -> listaBean.get(i).getId().equals(this.bean.getId()))
		      //.mapToObj(i -> listaBean.get(i))
		      .findFirst();
			
			if (!indice.isEmpty()) {
				AdmMenu obj = listaBean.get(indice.getAsInt());
				this.listaBean.set(indice.getAsInt(), this.service.update(obj));			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Menu Atualizado"));				
			}
		}

		PrimeFaces.current().executeScript("PF('editDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
	}

	public void updateTreeMenus() {
		List<AdmMenu> listaMenus = this.service.getRepository().findMenuRoot();

		AdmMenu menu = new AdmMenu();
		menu.setDescription("Menu do sistema");
		this.menuRaiz = new DefaultTreeNode<AdmMenu>(menu, null);
		for (AdmMenu itemMenu : listaMenus) {
			TreeNode<AdmMenu> m = new DefaultTreeNode<AdmMenu>(itemMenu, this.menuRaiz);
			mountSubMenu(itemMenu, m);
		}
	}

	private List<TreeNode<AdmMenu>> mountSubMenu(AdmMenu menu, TreeNode<AdmMenu> pMenu) {
		List<TreeNode<AdmMenu>> lstSubMenu = new ArrayList<TreeNode<AdmMenu>>();
		if (menu.getAdmSubMenus() != null) {
			for (AdmMenu subMenu : menu.getAdmSubMenus()) {
				if (subMenu.isSubMenu()) {
					TreeNode<AdmMenu> m = new DefaultTreeNode<AdmMenu>(subMenu, pMenu);
					mountSubMenu(subMenu, m);
				} else {
					new DefaultTreeNode<AdmMenu>(subMenu, pMenu);
				}
			}
		}
		return lstSubMenu;
	}
	
	public List<AdmMenu> getListaBean() {
		return listaBean;
	}

	public AdmMenu getBean() {
		return bean;
	}

	public void setBean(AdmMenu bean) {
		this.bean = bean;
	}
	
	public AdmMenu getSelecionadoBean() {
		return selecionadoBean;
	}

	public void setSelecionadoBean(AdmMenu selecionadoBean) {
		this.selecionadoBean = selecionadoBean;
	}

	public TreeNode<AdmMenu> getMenuRaiz() {
		return menuRaiz;
	}

	public void setMenuRaiz(TreeNode<AdmMenu> menuRaiz) {
		this.menuRaiz = menuRaiz;
	}

	public TreeNode<AdmMenu> getMenuSelecionado() {
		return menuSelecionado;
	}

	public void setMenuSelecionado(TreeNode<AdmMenu> menuSelecionado) {
		this.menuSelecionado = menuSelecionado;
	}

	public List<AdmPage> getListaAdmPagina() {
		return listaAdmPagina;
	}

	public void setListaAdmPagina(List<AdmPage> listaAdmPagina) {
		this.listaAdmPagina = listaAdmPagina;
	}

	public List<AdmMenu> getListaMenusParent() {
		return listaMenusParent;
	}

	public void setListaMenusParent(List<AdmMenu> listaMenusParent) {
		this.listaMenusParent = listaMenusParent;
	}

}
