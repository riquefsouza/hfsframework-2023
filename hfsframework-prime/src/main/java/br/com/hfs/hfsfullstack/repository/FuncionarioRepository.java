package br.com.hfs.hfsfullstack.repository;

import java.util.List;

import br.com.hfs.base.BaseRepository;
import br.com.hfs.hfsfullstack.model.Funcionario;

public class FuncionarioRepository extends BaseRepository<Funcionario, Long> {

	private static final long serialVersionUID = 1L;

	public FuncionarioRepository() {
		super(Funcionario.class);
	}
	
	public List<Funcionario> findPaginated(int pageNumber, int pageSize) {
		return findPaginated("vw_adm_funcionario", "cod_funcionario", pageNumber, pageSize);
	}

	public List<Funcionario> listByRange(int startInterval, int endInterval) {
		return listByRange("vw_adm_funcionario", "cod_funcionario", startInterval, endInterval);
	}
	
	public List<Funcionario> findByNomeLike(String nome){
		return queryList("Funcionario.findByNomeLike", nome + "%");
	}

}
