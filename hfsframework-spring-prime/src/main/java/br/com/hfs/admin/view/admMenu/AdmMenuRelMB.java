package br.com.hfs.admin.view.admMenu;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.service.AdmMenuService;
import br.com.hfs.base.report.BaseReportImpl;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.IBaseReport;
import jakarta.faces.view.ViewScoped;

@Component
//@Named
@ViewScoped
//@HandlingExpectedErrors
public class AdmMenuRelMB extends BaseViewReportController implements Serializable {
//	extends BaseViewReport<AdmMenu, Long, AdmMenuService> implements IBaseViewReport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmMenuService service;
	
	/** The forcar download. */
	private Boolean forceDownload;
	
	/** The report. */
	//@Inject
	//@ReportPath("AdmMenu")
	//private IBaseReport report;

	/**
	 * Instantiates a new adm menu rel MB.
	 */
	public AdmMenuRelMB() {
		super();
		this.forceDownload = false;
	}

	public void export() {
		Map<String, Object> params = getParameters();
		params.put("PARAMETER1", "");
		
		IBaseReport report = new BaseReportImpl("AdmMenu");
		super.export(report, service.findAll(), params, this.forceDownload);
	}

	public Boolean getForceDownload() {
		return forceDownload;
	}

	public void setForceDownload(Boolean forceDownload) {
		this.forceDownload = forceDownload;
	}

}
