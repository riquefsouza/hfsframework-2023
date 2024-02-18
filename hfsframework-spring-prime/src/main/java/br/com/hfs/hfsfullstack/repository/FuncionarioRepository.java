package br.com.hfs.hfsfullstack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.hfsfullstack.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	@Query(name = "Funcionario.findByNomeLike")
	public List<Funcionario> findByNomeLike(String nome);

	Page<Funcionario> findByNomeLike(String nome, Pageable pagination);

}
