package br.com.hfs.admin.view.admParameterCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.model.AdmParameterCategory;
import br.com.hfs.admin.service.AdmParameterCategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

@Component
//@Named
@ViewScoped
//@HandlingExpectedErrors
public class AdmParameterCategoryView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AdmParameterCategoryService service;
	
	private List<AdmParameterCategory> listaBean;
	
	private AdmParameterCategory bean;
	
	private List<AdmParameterCategory> selecionadosBean;

	@PostConstruct
	public void init() {
		this.listaBean = this.service.findAll();
		this.selecionadosBean = new ArrayList<>();
	}

	public List<AdmParameterCategory> getListaBean() {
		return listaBean;
	}

	public AdmParameterCategory getBean() {
		return bean;
	}

	public void setBean(AdmParameterCategory bean) {
		this.bean = bean;
	}

	public List<AdmParameterCategory> getSelecionadosBean() {
		return selecionadosBean;
	}

	public void setSelecionadosBean(List<AdmParameterCategory> selecionadosBean) {
		this.selecionadosBean = selecionadosBean;
	}

	public void adicionar() {
		this.bean = new AdmParameterCategory();
	}
	
	public void salvar() {
		if (this.bean.getId() == null) {
			this.listaBean.add(this.service.insert(this.bean));
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categoria do Parâmetro Adicionado"));
		} else {
			OptionalInt indice = IntStream
		      .range(0, listaBean.size())
		      .filter(i -> listaBean.get(i).getId().equals(this.bean.getId()))
		      //.mapToObj(i -> listaBean.get(i))
		      .findFirst();
			
			if (!indice.isEmpty()) {
				AdmParameterCategory obj = listaBean.get(indice.getAsInt());
				this.listaBean.set(indice.getAsInt(), this.service.update(obj));			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categoria do Parâmetro Atualizado"));				
			}
		}

		PrimeFaces.current().executeScript("PF('editDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
	}

	public void excluir() {
		this.service.delete(this.bean);
		this.listaBean.remove(this.bean);
		this.bean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categoria do Parâmetro Excluído"));
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
	}

	public String getDeleteButtonMessage() {
		if (hasSelecionadosBean()) {
			int size = this.selecionadosBean.size();
			return size > 1 ? size + " Categorias do Parâmetros selecionados" : "1 Categoria do Parâmetro selecionado";
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
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categorias dos Parâmetros Excluídos"));
		PrimeFaces.current().ajax().update("form:messages", "form:tabela");
		PrimeFaces.current().executeScript("PF('widgetTabela').clearFilters()");
	}

}
