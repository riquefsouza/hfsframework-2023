package br.com.hfs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentials {

	private String username;

	private String password;

	//@SuppressFBWarnings("EI_EXPOSE_REP")
	private List<String> authorities;

}
