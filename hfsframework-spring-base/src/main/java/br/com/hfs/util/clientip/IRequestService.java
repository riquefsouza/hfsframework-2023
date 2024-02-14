package br.com.hfs.util.clientip;

import jakarta.servlet.http.HttpServletRequest;

public interface IRequestService {
	
	String getClientIp(HttpServletRequest request);
	
}
