/**
 * <p><b>HFS Framework</b></p>
 * @author Henrique Figueiredo de Souza
 * @version 1.0
 * @since 2019
 */
package br.com.hfs.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseViewQuery<T, I extends Serializable, 
	B extends BaseService<T, I, ? extends JpaRepository<T, I>>> extends BaseViewController implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private String pageList;
	
	@Autowired
	private B service;

	private List<T> listEntity;

	@Autowired
	private T entity;
	
	/**
	 * Instantiates a new base view cadastro.
	 *
	 * @param pageList
	 *            the pagina listar
	 */
	public BaseViewQuery(String pageList){
		super();		
		this.pageList = pageList;
	}

	protected void updateDataTableList() {
		this.listEntity = service.findAll();
	}

	public String getPageList() {
		return pageList;
	}

	public String cancel() {
		return getDesktopPage();
	}

	public T getEntity() {
		return this.entity;
	}

	public void setEntity(T entidade) {
		this.entity = entidade;
	}

	public List<T> getListEntity() {
		return this.listEntity;
	}

	public void setListEntity(List<T> listEntity) {
		this.listEntity = listEntity;
	}

	public B getService() {
		return service;
	}

}
