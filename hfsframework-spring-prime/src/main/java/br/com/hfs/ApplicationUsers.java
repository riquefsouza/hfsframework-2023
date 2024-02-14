package br.com.hfs;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("application-users")
@Getter
@Setter
public class ApplicationUsers {

	//@SuppressFBWarnings("EI_EXPOSE_REP")
	private List<UserCredentials> usersCredentials;

}
