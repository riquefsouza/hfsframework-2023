package br.com.hfs.admin.view.admParameterCategory;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.service.AdmParameterCategoryService;
import br.com.hfs.base.report.BaseReportImpl;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.IBaseReport;
import jakarta.faces.view.ViewScoped;

//@Named
@Component
@ViewScoped
//@HandlingExpectedErrors
public class AdmParameterCategoryRelMB extends BaseViewReportController implements Serializable { 
//extends BaseViewReport<AdmParameterCategory, Long, AdmParameterCategoryService> {
//		implements IBaseViewReport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmParameterCategoryService service;

	private Boolean forceDownload;

	//@Inject
	//@ReportPath("AdmParameterCategory")
	//private IBaseReport report;

	public AdmParameterCategoryRelMB() {
		super();
		this.forceDownload = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.jus.trt1.hfsframework.base.IBaseViewRelatorio#exportar()
	 */
	public void export() {
		Map<String, Object> params = getParametros();
		params.put("PARAMETER1", "");

		IBaseReport report = new BaseReportImpl("AdmParameterCategory");
		super.export(report, service.findAll(), params, this.forceDownload);
		
	}

	public Boolean getForceDownload() {
		return forceDownload;
	}

	public void setForceDownload(Boolean forceDownload) {
		this.forceDownload = forceDownload;
	}
}
