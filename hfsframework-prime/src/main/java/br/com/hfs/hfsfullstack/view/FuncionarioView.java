package br.com.hfs.hfsfullstack.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

import br.com.hfs.hfsfullstack.model.Funcionario;
import br.com.hfs.hfsfullstack.service.FuncionarioService;
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
public class FuncionarioView implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean modoEditar = false;
	
	@Inject
	private FuncionarioService service;
	
	private Funcionario bean;
	
	private List<Funcionario> selecionadosBean;

	private FuncionarioLazyDataModel lazyModel;
	
	@PostConstruct
	public void init() {
		this.selecionadosBean = new ArrayList<>();
		atualizaListaDataTable();
	}

	private void atualizaListaDataTable() {
		lazyModel = new FuncionarioLazyDataModel(this.service);
	}

	public Funcionario getBean() {
		return bean;
	}

	public void setBean(Funcionario bean) {
		this.bean = bean;
	}

	public List<Funcionario> getSelecionadosBean() {
		return selecionadosBean;
	}

	public void setSelecionadosBean(List<Funcionario> selecionadosBean) {
		this.selecionadosBean = selecionadosBean;
	}

	public boolean isModoEditar() {
		return modoEditar;
	}

	public void limpar() {
		this.bean.clean();
	}

	public void adicionar() {
		this.modoEditar = true;
		this.bean = new Funcionario();
	}
	
	public void editar(Funcionario item) {
		this.modoEditar = true;
		this.bean = item;
	}

	public void cancelarEditar() {
	    this.modoEditar = false;
	}
	
	public void salvar() {
		if (this.bean.getId() == null) {
			//this.listaBean.add(this.service.insert(this.bean));
			this.service.insert(this.bean);
			atualizaListaDataTable();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Funcionário Adicionado"));
		} else {
			/*
			OptionalInt indice = IntStream
		      .range(0, listaBean.size())
		      .filter(i -> listaBean.get(i).getId().equals(this.bean.getId()))
		      //.mapToObj(i -> listaBean.get(i))
		      .findFirst();
			
			if (!indice.isEmpty()) {
				Funcionario obj = listaBean.get(indice.getAsInt());
				this.listaBean.set(indice.getAsInt(), this.service.update(obj));			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Funcionário Atualizado"));				
			}
			*/
			this.service.update(this.bean);
			atualizaListaDataTable();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Funcionário Atualizado"));
		}

		this.modoEditar = false;
	}

	public void chamarDialogExcluir(Funcionario item) {
		this.bean = item;
		PrimeFaces.current().executeScript("PF('excluirDialog').show()");
	}
	
	public void excluir() {
		this.service.delete(this.bean);
		//this.listaBean.remove(this.bean);
		atualizaListaDataTable();
		this.bean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Funcionário Excluído"));
	}

	public String getDeleteButtonMessage() {
		if (hasSelecionadosBean()) {
			int size = this.selecionadosBean.size();
			return size > 1 ? size + " Funcionários selecionados" : "1 Funcionário selecionado";
		}

		return "Excluir";
	}

	public boolean hasSelecionadosBean() {
		return this.selecionadosBean != null && !this.selecionadosBean.isEmpty();
	}

	public void deleteSelecionadosBean() {
		this.service.delete(this.selecionadosBean);
		//this.listaBean.removeAll(this.selecionadosBean);
		atualizaListaDataTable();
		this.selecionadosBean = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Funcionários Excluídos"));
		PrimeFaces.current().ajax().update("formFuncionario:messages", "formFuncionario:tabela");
		PrimeFaces.current().executeScript("PF('widgetTabela').clearFilters()");
	}

	public FuncionarioLazyDataModel getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(FuncionarioLazyDataModel lazyModel) {
		this.lazyModel = lazyModel;
	}

}
