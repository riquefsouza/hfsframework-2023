package br.com.hfs.admin.view.admParameterCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.model.AdmParameterCategory;
import br.com.hfs.admin.service.AdmParameterCategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

//@Named
@Component
@ViewScoped
//@HandlingExpectedErrors
public class AdmParameterCategoryView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmParameterCategoryService service;
	
	private List<AdmParameterCategory> listaBean;
	
	private AdmParameterCategory selecionadoBean;
	
	private List<AdmParameterCategory> selecionadosBean;

	@PostConstruct
	public void init() {
		this.listaBean = this.service.findAll();
		this.selecionadosBean = new ArrayList<>();
	}

	public List<AdmParameterCategory> getListaBean() {
		return listaBean;
	}

	public AdmParameterCategory getSelecionadoBean() {
		return selecionadoBean;
	}

	public void setSelecionadoBean(AdmParameterCategory selecionadoBean) {
		this.selecionadoBean = selecionadoBean;
	}

	public List<AdmParameterCategory> getSelecionadosBean() {
		return selecionadosBean;
	}

	public void setSelecionadosBean(List<AdmParameterCategory> selecionadosBean) {
		this.selecionadosBean = selecionadosBean;
	}

	public void adicionar() {
		this.selecionadoBean = new AdmParameterCategory();
	}
	
	public void salvar() {
		if (this.selecionadoBean.getId() == null) {
			this.listaBean.add(this.service.insert(this.selecionadoBean));
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Categoria do Parâmetro Adicionado"));
		} else {
			OptionalInt indice = IntStream
		      .range(0, listaBean.size())
		      .filter(i -> listaBean.get(i).getId().equals(this.selecionadoBean.getId()))
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
		this.service.delete(this.selecionadoBean);
		this.listaBean.remove(this.selecionadoBean);
		this.selecionadoBean = null;
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
