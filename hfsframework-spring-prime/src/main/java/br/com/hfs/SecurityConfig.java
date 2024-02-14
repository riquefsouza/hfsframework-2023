package br.com.hfs;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import br.com.hfs.security.HfsUserDetailsService;

//@SuppressFBWarnings("SPRING_CSRF_PROTECTION_DISABLED")
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(ApplicationUsers.class)
public class SecurityConfig {

	@Autowired
	private HfsUserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain configure(HttpSecurity http, MvcRequestMatcher.Builder mvc) {
		try {
			http.csrf((csrf) -> csrf.disable());
			http.authorizeHttpRequests((authorize) -> authorize.requestMatchers(mvc.pattern("/"))
				.permitAll()
				.requestMatchers(new AntPathRequestMatcher("/**.faces"))
				.permitAll()
				.requestMatchers(new AntPathRequestMatcher("/jakarta.faces.resource/**"))
				.permitAll()
				.anyRequest()
				.authenticated())
				.formLogin((formLogin) -> formLogin.loginPage("/login.faces")
					.permitAll()
					//.failureUrl("/login.faces?error=true")
					.failureUrl("/error.faces")
					.defaultSuccessUrl("/desktop.faces", true))
				.logout((logout) -> logout.logoutSuccessUrl("/login.faces").deleteCookies("JSESSIONID"));
			return http.build();
		}
		catch (Exception ex) {
			throw new BeanCreationException("Wrong spring security configuration", ex);
		}
	}

	@Scope("prototype")
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}
/*
	@Bean
	public InMemoryUserDetailsManager userDetailsService(ApplicationUsers applicationUsers) {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		InMemoryUserDetailsManager result = new InMemoryUserDetailsManager();
		for (UserCredentials userCredentials : applicationUsers.getUsersCredentials()) {
			result.createUser(User.builder()
				.username(userCredentials.getUsername())
				.password(encoder.encode(userCredentials.getPassword()))
				.authorities(userCredentials.getAuthorities().toArray(new String[0]))
				.build());
		}
		return result;
	}
*/	

	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
	
}
