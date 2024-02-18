package br.com.hfs.admin.view.admParameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.model.AdmParameter;
import br.com.hfs.admin.model.AdmParameterCategory;
import br.com.hfs.admin.service.AdmParameterCategoryService;
import br.com.hfs.admin.service.AdmParameterService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

@Component
//@Named
@ViewScoped
//@HandlingExpectedErrors
public class AdmParameterView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AdmParameterService service;
	
	private List<AdmParameter> listaBean;
	
	private AdmParameter bean;
	
	private List<AdmParameter> selecionadosBean;

	@Inject
	private AdmParameterCategoryService admParameterCategoryService;

	private List<AdmParameterCategory> listAdmParameterCategory;

	@PostConstruct
	public void init() {
		this.listaBean = this.service.findAll();
		this.selecionadosBean = new ArrayList<>();
		listAdmParameterCategory = admParameterCategoryService.findAll();
	}

	public List<AdmParameter> getListaBean() {
		return listaBean;
	}

	public AdmParameter getBean() {
		return bean;
	}

	public void setBean(AdmParameter bean) {
		this.bean = bean;
	}

	public List<AdmParameter> getSelecionadosBean() {
		return selecionadosBean;
	}

	public void setSelecionadosBean(List<AdmParameter> selecionadosBean) {
		this.selecionadosBean = selecionadosBean;
	}

	public void adicionar() {
		this.bean = new AdmParameter();
		setAdmParameterCategory(this.bean);
	}
	
	public void salvar() {
		this.bean.setIdAdmParameterCategory(this.bean.getAdmParameterCategory().getId());
		
		if (this.bean.getId() == null) {
			this.listaBean.add(this.service.insert(this.bean));
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Parâmetro Adicionado"));
		} else {
			OptionalInt indice = IntStream
		      .range(0, listaBean.size())
		      .filter(i -> listaBean.get(i).getId().equals(this.bean.getId()))
		      //.mapToObj(i -> listaBean.get(i))
		      .findFirst();
			
			if (!indice.isEmpty()) {
				AdmParameter obj = listaBean.get(indice.getAsInt());
				this.listaBean.set(indice.getAsInt(), this.service.update(obj));			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Parâmetro Atualizado"));				
			}
		}

		PrimeFaces.current().executeScript("PF('editDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
	}

	public void excluir() {
		this.service.delete(this.bean);
		this.listaBean.remove(this.bean);
		this.bean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Parâmetro Excluído"));
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
	}

	public String getDeleteButtonMessage() {
		if (hasSelecionadosBean()) {
			int size = this.selecionadosBean.size();
			return size > 1 ? size + " Parâmetros selecionados" : "1 Parâmetro selecionado";
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
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Parâmetros Excluídos"));
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
		PrimeFaces.current().executeScript("PF('widgetTabela').clearFilters()");
	}

	public void selectAdmParameterCategory(AdmParameter bean) {
		AdmParameterCategory admParametroCategoria = admParameterCategoryService
				.findById(bean.getAdmParameterCategory().getId()).get();
		bean.setAdmParameterCategory(admParametroCategoria);
	}
	
	private void setAdmParameterCategory(AdmParameter bean) {
		if (bean.getAdmParameterCategory() != null && listAdmParameterCategory.size() > 0) {
			bean.getAdmParameterCategory().setId(listAdmParameterCategory.get(0).getId());
			selectAdmParameterCategory(bean);
		}		
	}
	
	public List<AdmParameterCategory> getListAdmParameterCategory() {
		return listAdmParameterCategory;
	}

	public void setListaAdmParameterCategory(List<AdmParameterCategory> listAdmParameterCategory) {
		this.listAdmParameterCategory = listAdmParameterCategory;
	}
	
}
