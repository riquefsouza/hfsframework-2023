package br.com.hfs.admin.view.admPage;

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
public class AdmPageMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean modoEditar = false;
	
	@Inject
	private AdmPageService service;
	
	private List<AdmPage> listaBean;
	
	private AdmPage bean;
	
	private List<AdmPage> selecionadosBean;

	@Inject
	private AdmProfileService admProfileService;

	private DualListModel<AdmProfile> dualListAdmProfile;

	private List<AdmProfile> listaAdmProfile;

	@PostConstruct
	public void init() {
		this.listaBean = this.service.findAll();
		this.selecionadosBean = new ArrayList<>();
	}

	private void loadAdmPerfis(AdmPage bean) {
	    if (bean == null) {
			this.listaAdmProfile = admProfileService.findAll();
			this.dualListAdmProfile = new DualListModel<AdmProfile>(this.listaAdmProfile, new ArrayList<AdmProfile>());
	    } else {  		
			List<AdmProfile> listaAdmProfileSelecionado = bean.getId() == null ? new ArrayList<AdmProfile>()
					: this.service.findProfilesByPage(bean);
			this.listaAdmProfile = admProfileService.findAll();
			this.listaAdmProfile.removeAll(listaAdmProfileSelecionado);
			this.dualListAdmProfile = new DualListModel<AdmProfile>(this.listaAdmProfile, listaAdmProfileSelecionado);
	    }
	}

	public List<AdmPage> getListaBean() {
		return listaBean;
	}

	public AdmPage getBean() {
		return bean;
	}

	public void setBean(AdmPage bean) {
		this.bean = bean;
	}

	public List<AdmPage> getSelecionadosBean() {
		return selecionadosBean;
	}

	public void setSelecionadosBean(List<AdmPage> selecionadosBean) {
		this.selecionadosBean = selecionadosBean;
	}

	public boolean isModoEditar() {
		return modoEditar;
	}

	public void limpar() {
		this.bean.clean();
		this.loadAdmPerfis(null);
	}

	public void adicionar() {
		this.modoEditar = true;
		this.bean = new AdmPage();
		this.loadAdmPerfis(null);
	}
	
	public void editar(AdmPage item) {
		this.modoEditar = true;
		this.bean = item;
		this.loadAdmPerfis(bean);
	}

	public void cancelarEditar() {
	    this.modoEditar = false;
	}
	
	public void salvar() {
		getBean().setAdmProfiles(new HashSet<AdmProfile>(this.dualListAdmProfile.getTarget()));
		
		if (this.bean.getId() == null) {
			this.listaBean.add(this.service.insert(this.bean));
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Página Adicionada"));
		} else {
			OptionalInt indice = IntStream
		      .range(0, listaBean.size())
		      .filter(i -> listaBean.get(i).getId().equals(this.bean.getId()))
		      //.mapToObj(i -> listaBean.get(i))
		      .findFirst();
			
			if (!indice.isEmpty()) {
				AdmPage obj = listaBean.get(indice.getAsInt());
				this.listaBean.set(indice.getAsInt(), this.service.update(obj));			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Página Atualizada"));				
			}
		}

		this.modoEditar = false;
	}

	public void chamarDialogExcluir(AdmPage item) {
		this.bean = item;
		PrimeFaces.current().executeScript("PF('excluirDialog').show()");
	}
	
	public void excluir() {
		this.service.delete(this.bean);
		this.listaBean.remove(this.bean);
		this.bean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Página Excluída"));
	}

	public String getDeleteButtonMessage() {
		if (hasSelecionadosBean()) {
			int size = this.selecionadosBean.size();
			return size > 1 ? size + " Páginas selecionadas" : "1 Página selecionada";
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
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Páginas Excluídas"));
		PrimeFaces.current().ajax().update("formAdmPage:messages", "formAdmPage:tabela");
		PrimeFaces.current().executeScript("PF('widgetTabela').clearFilters()");
	}

	public DualListModel<AdmProfile> getDualListAdmProfile() {
		return dualListAdmProfile;
	}
		
	public void setDualListAdmProfile(DualListModel<AdmProfile> dualListAdmProfile) {
		this.dualListAdmProfile = dualListAdmProfile;
	}
	
}
