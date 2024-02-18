package br.com.hfs.hfsfullstack.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.hfs.base.BaseService;
import br.com.hfs.hfsfullstack.model.Funcionario;
import br.com.hfs.hfsfullstack.repository.FuncionarioRepository;

@Service
public class FuncionarioService extends BaseService<Funcionario, Long, FuncionarioRepository> {

	private static final long serialVersionUID = 1L;
	
	public FuncionarioService() {
		super(Funcionario.class);
	}


	public List<Funcionario> findByNomeLike(String description){
		return repository.findByNomeLike(description + "%");
	}
	
}
