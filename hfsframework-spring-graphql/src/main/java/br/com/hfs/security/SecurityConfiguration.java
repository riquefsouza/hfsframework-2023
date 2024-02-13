package br.com.hfs.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.hfs.admin.repository.AdmProfileRepository;
import br.com.hfs.admin.repository.AdmUserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private HfsUserDetailsService userDetailsService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AdmUserRepository userRepository;
	
	@Autowired
	private AdmProfileRepository profileRepository;
	
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    	return http
    			.cors(cors -> {
    				cors.configurationSource(corsConfigurationSource());
    			})
                .authorizeHttpRequests(authorize -> {
                    authorize
            		//.requestMatchers(HttpMethod.GET, "/api/v1").permitAll()
            		//.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
            		.requestMatchers(HttpMethod.POST, "/auth", "/auth/**").permitAll()
            		//.requestMatchers(HttpMethod.POST, "/graphql", "/graphql/**", "/graphiql", "/graphiql/**").permitAll()
            		.anyRequest().authenticated();
                })
                .csrf(csrf -> {
                    csrf.disable();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new TokenFilter(tokenService, userRepository, profileRepository), 
                		UsernamePasswordAuthenticationFilter.class)
                .build();    	
    }	
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
    
	private CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*"));
	    configuration.setAllowedMethods(Arrays.asList("*"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));
	    //in case authentication is enabled this flag MUST be set, otherwise CORS requests will fail
	    configuration.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}	

}
