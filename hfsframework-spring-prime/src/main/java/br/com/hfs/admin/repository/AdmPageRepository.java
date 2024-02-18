package br.com.hfs.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.admin.model.AdmPage;
import br.com.hfs.admin.model.AdmProfile;

public interface AdmPageRepository extends JpaRepository<AdmPage, Long> {

	Page<AdmPage> findByDescriptionLike(String description, Pageable pagination);
	
	List<AdmPage> findByDescriptionLike(String description);
	
	@Query(name = "AdmPage.findProfilesByPage")
	public List<AdmProfile> findProfilesByPage(AdmPage page);
	
}
