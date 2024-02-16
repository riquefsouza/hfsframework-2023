package br.com.hfs.admin.view.admProfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;

import br.com.hfs.admin.model.AdmPage;
import br.com.hfs.admin.model.AdmProfile;
import br.com.hfs.admin.service.AdmPageService;
import br.com.hfs.admin.service.AdmProfileService;
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
public class AdmProfileView implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean modoEditar = false;
	
	@Inject
	private AdmProfileService service;
	
	private List<AdmProfile> listaBean;
	
	private AdmProfile bean;
	
	private List<AdmProfile> selecionadosBean;

	@Inject
	private AdmPageService admPageService;

	private DualListModel<AdmPage> dualListAdmPage;

	private List<AdmPage> listaAdmPage;

	@PostConstruct
	public void init() {
		this.listaBean = this.service.findAll();
		this.selecionadosBean = new ArrayList<>();
	}

	private void loadAdmPaginas(AdmProfile bean) {
	    if (bean == null) {
			this.listaAdmPage = admPageService.findAll();
			this.dualListAdmPage = new DualListModel<AdmPage>(this.listaAdmPage, new ArrayList<AdmPage>());
	    } else {  		
			List<AdmPage> listaAdmPageSelecionado = bean.getId() == null ? new ArrayList<AdmPage>()
					: this.service.findPagesByProfile(bean);
			this.listaAdmPage = admPageService.findAll();
			this.listaAdmPage.removeAll(listaAdmPageSelecionado);
			this.dualListAdmPage = new DualListModel<AdmPage>(this.listaAdmPage, listaAdmPageSelecionado);
	    }
	}

	public List<AdmProfile> getListaBean() {
		return listaBean;
	}

	public AdmProfile getBean() {
		return bean;
	}

	public void setBean(AdmProfile bean) {
		this.bean = bean;
	}

	public List<AdmProfile> getSelecionadosBean() {
		return selecionadosBean;
	}

	public void setSelecionadosBean(List<AdmProfile> selecionadosBean) {
		this.selecionadosBean = selecionadosBean;
	}

	public boolean isModoEditar() {
		return modoEditar;
	}

	public void limpar() {
		this.bean.clean();
		this.loadAdmPaginas(null);
	}

	public void adicionar() {
		this.modoEditar = true;
		this.bean = new AdmProfile();
		this.loadAdmPaginas(null);
	}
	
	public void editar(AdmProfile item) {
		this.modoEditar = true;
		this.bean = item;
		this.loadAdmPaginas(bean);
	}

	public void cancelarEditar() {
	    this.modoEditar = false;
	}
	
	public void salvar() {
		getBean().setAdmPages(new HashSet<AdmPage>(this.dualListAdmPage.getTarget()));
		
		if (this.bean.getId() == null) {
			this.listaBean.add(this.service.insert(this.bean));
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Perfil Adicionado"));
		} else {
			OptionalInt indice = IntStream
		      .range(0, listaBean.size())
		      .filter(i -> listaBean.get(i).getId().equals(this.bean.getId()))
		      //.mapToObj(i -> listaBean.get(i))
		      .findFirst();
			
			if (!indice.isEmpty()) {
				AdmProfile obj = listaBean.get(indice.getAsInt());
				this.listaBean.set(indice.getAsInt(), this.service.update(obj));			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Perfil Atualizado"));				
			}
		}

		this.modoEditar = false;
	}

	public void chamarDialogExcluir(AdmProfile item) {
		this.bean = item;
		PrimeFaces.current().executeScript("PF('excluirDialog').show()");
	}
	
	public void excluir() {
		this.service.delete(this.bean);
		this.listaBean.remove(this.bean);
		this.bean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Perfil Excluído"));
	}

	public String getDeleteButtonMessage() {
		if (hasSelecionadosBean()) {
			int size = this.selecionadosBean.size();
			return size > 1 ? size + " Perfis selecionados" : "1 Perfil selecionado";
		}

		return "Excluir";
	}

	public boolean hasSelecionadosBean() {
		return this.selecionadosBean != null && !this.selecionadosBean.isEmpty();
	}

	public void deleteSelecionadosBean() {
		this.service.delete(this.selecionadosBean);
		this.listaBean.removeAll(this.selecionadosBean);
		this.selecionadosBean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Perfis Excluídos"));
		PrimeFaces.current().ajax().update("formAdmProfile:messages", "formAdmProfile:tabela");
		PrimeFaces.current().executeScript("PF('widgetTabela').clearFilters()");
	}

	public DualListModel<AdmPage> getDualListAdmPage() {
		return dualListAdmPage;
	}
		
	public void setDualListAdmPage(DualListModel<AdmPage> dualListAdmPage) {
		this.dualListAdmPage = dualListAdmPage;
	}
	
}
