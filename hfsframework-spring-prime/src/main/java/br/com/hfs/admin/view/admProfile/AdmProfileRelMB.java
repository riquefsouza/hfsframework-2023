package br.com.hfs.admin.view.admProfile;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.service.AdmProfileService;
import br.com.hfs.base.report.BaseReportImpl;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.IBaseReport;
import jakarta.faces.view.ViewScoped;

@Component
//@Named
@ViewScoped
//@HandlingExpectedErrors
public class AdmProfileRelMB extends BaseViewReportController implements Serializable {
//	extends BaseViewReport<AdmProfile, Long, AdmProfileService> implements IBaseViewReport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmProfileService service;
	
	private Boolean forceDownload;
	
	//@Inject
	//@ReportPath("AdmProfile")
	//private IBaseReport report;

	public AdmProfileRelMB() {
		super();
		this.forceDownload = false;
	}

	public void export() {
		Map<String, Object> params = getParameters();
		params.put("PARAMETER1", "");

		IBaseReport report = new BaseReportImpl("AdmProfile");
		super.export(report, service.findAll(), params, this.forceDownload);
	}

	public Boolean getForceDownload() {
		return forceDownload;
	}

	public void setForceDownload(Boolean forceDownload) {
		this.forceDownload = forceDownload;
	}

}
