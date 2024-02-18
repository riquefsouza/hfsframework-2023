/**
 * <p><b>HFS Framework</b></p>
 * @author Henrique Figueiredo de Souza
 * @version 1.0
 * @since 2019
 */
package br.com.hfs.admin.view.admPage;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.service.AdmPageService;
import br.com.hfs.base.report.BaseReportImpl;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.IBaseReport;
import jakarta.faces.view.ViewScoped;

@Component
//@Named
@ViewScoped
//@HandlingExpectedErrors
public class AdmPageRelMB extends BaseViewReportController implements Serializable {
//	extends BaseViewReport<AdmPage, Long, AdmPageService> implements IBaseViewReport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmPageService service;
	
	private Boolean forceDownload;
	
	//@Inject
	//@ReportPath("AdmPage")
	//private IBaseReport report;

	public AdmPageRelMB() {
		super();
		this.forceDownload = false;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt1.hfsframework.base.IBaseViewRelatorio#exportar()
	 */
	public void export() {
		Map<String, Object> params = getParameters();
		params.put("PARAMETER1", "");

		IBaseReport report = new BaseReportImpl("AdmPage");
		super.export(report, service.findAll(), params, this.forceDownload);
	}

	public Boolean getForceDownload() {
		return forceDownload;
	}

	public void setForceDownload(Boolean forceDownload) {
		this.forceDownload = forceDownload;
	}

}
