package br.com.hfs.hfsfullstack.view;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ComparatorUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import br.com.hfs.base.BaseLazyModel;
import br.com.hfs.hfsfullstack.model.Funcionario;
import br.com.hfs.hfsfullstack.service.FuncionarioService;
import jakarta.faces.context.FacesContext;

public class FuncionarioLazyDataModel extends LazyDataModel<Funcionario> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private BaseLazyModel<Funcionario, Long, FuncionarioService> baseLazyModel;

	public FuncionarioLazyDataModel(FuncionarioService bc) {
		super();
		this.baseLazyModel = new BaseLazyModel<Funcionario, Long, FuncionarioService>(bc);
	}

	/* (non-Javadoc)
	 * @see org.primefaces.model.LazyDataModel#getRowData(java.lang.String)
	 */
	@Override
	public Funcionario getRowData(String rowKey) {
		for (Funcionario funcionario : this.baseLazyModel.getDatasource()) {
			if (funcionario.getId().toString().equals(rowKey))
				return funcionario;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.primefaces.model.LazyDataModel#getRowKey(java.lang.Object)
	 */
	@Override
	public String getRowKey(Funcionario funcionario) {
		return String.valueOf(funcionario.getId());
	}

	/* (non-Javadoc)
	 * @see org.primefaces.model.LazyDataModel#load(int, int, java.lang.String, org.primefaces.model.SortOrder, java.util.Map)
	 */
	@Override
	public List<Funcionario> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		List<Funcionario> data = this.baseLazyModel.load(first, pageSize, sortBy, filterBy, false);

		// sort
        if (!sortBy.isEmpty()) {
            List<Comparator<Funcionario>> comparators = sortBy.values().stream()
                    .map(o -> new FuncionarioLazySorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<Funcionario> cp = ComparatorUtils.chainedComparator(comparators);
            data.sort(cp);
        }		

		if (filterBy.keySet().size() > 0 && this.baseLazyModel.isFound()) {
			this.setRowCount(data.size());
		} else {
			this.setRowCount(this.baseLazyModel.getDataSize());
		}

		return data;
	}
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return (int) this.baseLazyModel.getDatasource().stream()
				.filter(o -> this.baseLazyModel.filter(FacesContext.getCurrentInstance(), filterBy.values(), o)).count();
	}
	
}
