package br.com.hfs.admin.view.admUser;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.service.AdmUserService;
import br.com.hfs.base.report.BaseReportImpl;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.IBaseReport;
import jakarta.faces.view.ViewScoped;

@Component
//@Named
@ViewScoped
//@HandlingExpectedErrors
public class AdmUserRelMB extends BaseViewReportController implements Serializable {
//	extends BaseViewReport<AdmUser, Long, AdmUserService> implements IBaseViewReport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmUserService service;
	
	private Boolean forceDownload;
	
	/* The relatorio. */
	//@Inject
	//@ReportPath("AdmUser")
	//private IBaseReport report;

	/**
	 * Instantiates a new adm parametro categoria rel MB.
	 */
	public AdmUserRelMB() {
		super();
		this.forceDownload = false;
	}
	
	public void export() {
		Map<String, Object> params = getParameters();
		params.put("PARAMETER1", "");

		IBaseReport report = new BaseReportImpl("AdmUser");
		super.export(report, service.findAll(), params, this.forceDownload);
	}

	public Boolean getForceDownload() {
		return forceDownload;
	}

	public void setForceDownload(Boolean forceDownload) {
		this.forceDownload = forceDownload;
	}

}
