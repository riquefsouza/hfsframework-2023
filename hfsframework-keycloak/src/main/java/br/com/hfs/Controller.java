package br.com.hfs;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import org.keycloak.AuthorizationContext;
import org.keycloak.representations.idm.authorization.Permission;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Named
@ViewScoped
public class Controller implements Serializable {
	
	private static final long serialVersionUID = 1L;

    public boolean isLoggedIn() {    	
    	//return this.getUser()!=null;
    	return getAuthzContext().isGranted();
    }
    
    public AuthorizationContext getAuthzContext() {
    	HttpServletRequest request = (HttpServletRequest) FacesContext
			    .getCurrentInstance().getExternalContext().getRequest();
    			
    	AuthorizationContext authzContext = (AuthorizationContext) request.getAttribute(AuthorizationContext.class.getName());

    	return authzContext;
    }
    
    public Principal getUser() {    	
    	HttpServletRequest request = (HttpServletRequest) FacesContext
			    .getCurrentInstance().getExternalContext().getRequest();

		return request.getUserPrincipal();    	
    }
    
    public List<Permission> getPermissions() {    	
    	return getAuthzContext().getPermissions();
    }
    
    public void logout() throws ServletException, IOException {
    	HttpServletRequest request = (HttpServletRequest) FacesContext
			    .getCurrentInstance().getExternalContext().getRequest();

    	HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();		

		request.logout();
		response.sendRedirect(request.getContextPath());
    }
    
}
