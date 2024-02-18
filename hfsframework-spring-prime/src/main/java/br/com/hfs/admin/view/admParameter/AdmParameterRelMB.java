/**
 * <p><b>HFS Framework</b></p>
 * @author Henrique Figueiredo de Souza
 * @version 1.0
 * @since 2019
 */
package br.com.hfs.admin.view.admParameter;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hfs.admin.service.AdmParameterService;
import br.com.hfs.base.report.BaseReportImpl;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.IBaseReport;
import jakarta.faces.view.ViewScoped;

@Component
//@Named
@ViewScoped
//@HandlingExpectedErrors
public class AdmParameterRelMB extends BaseViewReportController implements Serializable { 
//	extends BaseViewReport<AdmParameter, Long, AdmParameterService> implements IBaseViewReport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmParameterService service;
	
	/** The forcar download. */
	private Boolean forceDownload;
	
	/* The relatorio. */
	//@Inject
	//@ReportPath("AdmParameter")
	//private IBaseReport report;

	public AdmParameterRelMB() {
		super();
		this.forceDownload = false;
	}
	
	public void export() {
		Map<String, Object> params = getParameters();
		params.put("PARAMETER1", "");

		IBaseReport report = new BaseReportImpl("AdmParameter");
		super.export(report, service.findAll(), params, this.forceDownload);
	}

	public Boolean getForceDownload() {
		return forceDownload;
	}

	public void setForceDownload(Boolean forceDownload) {
		this.forceDownload = forceDownload;
	}

}
