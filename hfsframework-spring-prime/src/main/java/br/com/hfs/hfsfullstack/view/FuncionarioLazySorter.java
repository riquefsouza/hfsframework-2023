package br.com.hfs.hfsfullstack.view;

import org.primefaces.model.SortOrder;

import br.com.hfs.base.BaseLazySorter;
import br.com.hfs.hfsfullstack.model.Funcionario;

public class FuncionarioLazySorter extends BaseLazySorter<Funcionario> {

	public FuncionarioLazySorter(String sortField, SortOrder sortOrder) {
		super(sortField, sortOrder);
	}

}