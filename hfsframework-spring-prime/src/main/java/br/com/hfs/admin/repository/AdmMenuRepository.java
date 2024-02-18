package br.com.hfs.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.admin.model.AdmMenu;
import br.com.hfs.admin.model.AdmPage;

public interface AdmMenuRepository extends JpaRepository<AdmMenu, Long> {

	@Query(name = "AdmMenu.findChildrenMenus")
	List<AdmMenu> findChildrenMenus(AdmMenu menu);
	
	Page<AdmMenu> findByDescriptionLike(String description, Pageable pagination);
	
	List<AdmMenu> findByDescriptionLike(String description);
	
	@Query(name = "AdmMenu.findMenuRoot")
	public List<AdmMenu> findMenuRoot();

	@Query(name = "AdmMenu.findMenuRootByDescription")
	public List<AdmMenu> findMenuRootByDescription(String nomeItemMenu);
	
	@Query(name = "AdmMenu.countMenuByPage")
	public Integer countMenuByPage(AdmPage page);
	
	@Query(name = "AdmMenu.findAdminMenuParentByPage")
	public List<AdmMenu> findAdminMenuParentByPage(AdmPage page);

	@Query(name = "AdmMenu.findMenuParentByPage")
	public List<AdmMenu> findMenuParentByPage(AdmPage page);
	
	@Query(name = "AdmMenu.findPageByMenu")
	public AdmPage findPageByMenu(AdmPage page, Long idMenu);
}
