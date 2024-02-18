package br.com.hfs.view.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.hfs.UserCredentials;
import br.com.hfs.admin.vo.AuthenticatedUserVO;
import br.com.hfs.admin.vo.PermissionVO;
import br.com.hfs.admin.vo.ProfileVO;
import jakarta.enterprise.context.SessionScoped;

@Component
@SessionScoped
public class App implements Serializable {

	private static final long serialVersionUID = 1L;
	private String theme = "saga";
    private boolean darkMode = false;
    private String inputStyle = "outlined";
    private Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
    
    public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getTheme() {
        return theme;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getInputStyle() {
        return inputStyle;
    }

    public void setInputStyle(String inputStyle) {
        this.inputStyle = inputStyle;
    }

    public String getInputStyleClass() {
        return this.inputStyle.equals("filled") ? "ui-input-filled" : "";
    }

    public void changeTheme(String theme, boolean darkMode) {
        this.theme = theme;
        this.darkMode = darkMode;
    }

    public String getThemeImage() {
    	String result = getTheme();
    	switch (result) {
		case "nova-light":
			result = "nova.png";
			break;
		case "nova-colored":
			result = "nova-accent.png";
			break;
		case "nova-dark":
			result = "nova-alt.png";
			break;
		case "bootstrap4-blue-light":
            result = "bootstrap4-light-blue.svg";
            break;
        case "bootstrap4-blue-dark":
            result = "bootstrap4-dark-blue.svg";
            break;
        case "bootstrap4-purple-light":
            result = "bootstrap4-light-purple.svg";
            break;
        case "bootstrap4-purple-dark":
            result = "bootstrap4-dark-purple.svg";
            break;
        default:
            result += ".png";
			break;
		}
    	return result;
    }
    
    public UserCredentials getUserAuthenticated() {
    	UserCredentials user = new UserCredentials();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    user.setUsername(authentication.getName());
    	    if (authentication.getCredentials()!=null) {
    	    	user.setPassword(authentication.getCredentials().toString());	
    	    }    	    
    	    
    	    List<String> authorities = new ArrayList<String>();
    	    authentication.getAuthorities().forEach(item -> authorities.add(item.getAuthority()));    	    
    	    user.setAuthorities(authorities);
    	}
    	return user;
    }
    
    public AuthenticatedUserVO getAuthenticatedUser() {
    	ProfileVO perfil = new ProfileVO();
    	UserCredentials uc = getUserAuthenticated();
    	AuthenticatedUserVO vo = new AuthenticatedUserVO();
    	vo.setUserName(uc.getUsername());
    	uc.getAuthorities().forEach(item -> {
    		perfil.setDescription(item);
    	});
		
    	if (!perfil.getDescription().isEmpty()) {
        	PermissionVO perm = new PermissionVO();
        	perm.setProfile(perfil);    	
        	vo.getListPermission().add(perm);    		
    	}
    	vo.getUser().setLogin(vo.getUserName());
    	
    	return vo;
    }
    
}
