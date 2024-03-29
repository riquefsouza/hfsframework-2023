package br.com.hfs.admin.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hfs.admin.controller.dto.AdmProfileDTO;
import br.com.hfs.admin.controller.dto.MenuItemDTO;
import br.com.hfs.admin.controller.dto.ParamDTO;
import br.com.hfs.admin.controller.form.AdmProfileForm;
import br.com.hfs.admin.model.AdmProfile;
import br.com.hfs.admin.service.AdmProfileService;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.ReportParamsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admProfile")
public class AdmProfileRestController extends BaseViewReportController {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmProfileService admProfileService;

	@GetMapping("/paged")
	@Cacheable(value = "admProfileControllerList")
	public Page<AdmProfileDTO> listPaged(@RequestParam(required = false) String description, 
			@PageableDefault(page = 0, size = 20, direction = Direction.ASC, sort = "id") Pageable pagination) {
		//@RequestParam int page, @RequestParam int size, @RequestParam String fieldToSort) {
	
		//Pageable pagination = PageRequest.of(page, size, Direction.ASC, fieldToSort);
		
		if (description == null) {
			Page<AdmProfile> profiles = admProfileService.findAll(pagination);
			return AdmProfileDTO.convert(profiles);
		} else {
			Page<AdmProfile> profiles = admProfileService.findByDescriptionLike(description + "%", pagination);
			return AdmProfileDTO.convert(profiles);
		}
	}
	
	@GetMapping()
	@Cacheable(value = "admProfileControllerList")
	public List<AdmProfileDTO> listAll(@RequestParam(required = false) String description) {		
		if (description == null) {
			List<AdmProfile> objList = admProfileService.findAll();
			return AdmProfileDTO.convert(objList);
		} else {
			List<AdmProfile> objList = admProfileService.findByDescriptionLike(description + "%");
			return AdmProfileDTO.convert(objList);
		}
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "admProfileControllerList", allEntries = true)
	public ResponseEntity<AdmProfileDTO> save(@RequestBody @Valid AdmProfileForm form, UriComponentsBuilder uriBuilder) {
		AdmProfile obj = form.convert();
		admProfileService.insert(obj);
		
		URI uri = uriBuilder.path("/api/v1/admProfile/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(new AdmProfileDTO(obj));
	}

	@GetMapping("{id}")
	public ResponseEntity<AdmProfileDTO> get(@PathVariable Long id) {
		Optional<AdmProfile> bean = admProfileService.findById(id);
		if (bean.isPresent()) {
			return ResponseEntity.ok(new AdmProfileDTO(bean.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("{id}")
	@Transactional
	@CacheEvict(value = "admProfileControllerList", allEntries = true)
	public ResponseEntity<AdmProfileDTO> update(@PathVariable Long id, @RequestBody @Valid AdmProfileForm form){
		Optional<AdmProfile> bean = admProfileService.findById(id);
		if (bean.isPresent()) {
			AdmProfile profile = form.update(id, admProfileService);
			return ResponseEntity.ok(new AdmProfileDTO(profile));
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("{id}")
	@Transactional
	@CacheEvict(value = "admProfileControllerList", allEntries = true)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<AdmProfile> bean = admProfileService.findById(id);
		if (bean.isPresent()) {
			admProfileService.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/mountMenu")
	public List<MenuItemDTO> mountMenu(@RequestBody List<Long> listaIdProfile){
		return admProfileService.mountMenuItem(listaIdProfile);
	}

	@GetMapping("/findProfilesByPage/{pageId}")
	@Cacheable(value = "admProfileControllerList")
	public List<AdmProfileDTO> findProfilesByPage(@PathVariable Long pageId) {		
		List<AdmProfile> objList = admProfileService.findProfilesByPage(pageId);
		return AdmProfileDTO.convert(objList);
	}
	
	@GetMapping("/findProfilesByUser/{userId}")
	@Cacheable(value = "admProfileControllerList")
	public List<AdmProfileDTO> findProfilesByUser(@PathVariable Long userId) {		
		List<AdmProfile> objList = admProfileService.findProfilesByUser(userId);
		return AdmProfileDTO.convert(objList);
	}
	
	//@ApiOperation("Export Report")
	@PostMapping(value = "/report", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ByteArrayResource> report(HttpServletRequest request, @RequestBody ReportParamsDTO reportParamsDTO) {
		reportParamsDTO.getParams().add(new ParamDTO("PARAMETER1", ""));
		reportParamsDTO.setReportName("AdmProfile");
		return exportReport(reportParamsDTO, admProfileService.findAll());
	}	
	
}
