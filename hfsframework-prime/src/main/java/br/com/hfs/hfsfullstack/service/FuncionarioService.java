package br.com.hfs.hfsfullstack.service;

import java.util.List;

import br.com.hfs.base.BaseService;
import br.com.hfs.hfsfullstack.model.Funcionario;
import br.com.hfs.hfsfullstack.repository.FuncionarioRepository;

public class FuncionarioService extends BaseService<Funcionario, Long, FuncionarioRepository> {

	private static final long serialVersionUID = 1L;

	public List<Funcionario> findByNomeLike(String description){
		return repository.findByNomeLike(description);
	}
	
	public List<Funcionario> findPaginated(int pageNumber, int pageSize) {
		return repository.findPaginated(pageNumber, pageSize);
	}

	public List<Funcionario> listByRange(int startInterval, int endInterval) {
		return repository.listByRange(startInterval, endInterval);
	}
}
