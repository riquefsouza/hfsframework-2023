package br.com.hfs.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.hfs.admin.model.AdmProfile;
import br.com.hfs.admin.model.AdmUser;
import br.com.hfs.admin.repository.AdmProfileRepository;
import br.com.hfs.admin.repository.AdmUserRepository;

public class TokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	
	private AdmUserRepository userRepository;

	private AdmProfileRepository profileRepository;
	
	public TokenFilter(TokenService tokenService, AdmUserRepository userRepository, AdmProfileRepository profileRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
		}
		
		String token = authHeader.substring(7, authHeader.length());
		
		boolean valid = tokenService.isValidToken(token);
		
		if (valid) {
			authenticateUser(token);
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticateUser(String token) {
		Long idUser = tokenService.getIdUser(token);
		Optional<AdmUser> user = userRepository.findById(idUser);
		Set<AdmProfile> roles = new HashSet<AdmProfile>();
		if (user.isPresent()) {
			roles = new HashSet<AdmProfile>(profileRepository.findProfilesByUser(user.get().getId()));
		}
		HfsUserDetails hfsUserDetails = new HfsUserDetails(user.get(), roles);
		
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(hfsUserDetails, null, hfsUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
