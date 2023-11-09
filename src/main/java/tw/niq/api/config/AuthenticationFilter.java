package tw.niq.api.config;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tw.niq.api.domain.User;
import tw.niq.api.model.UserModel;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Autowired
	CustomSimpleUrlAuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.debug("AuthenticationFilter - attemptAuthentication");
		try {
			UserModel userModel = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
			String username = userModel.getUsername();
			String password = userModel.getPassword();
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
			return getAuthenticationManager().authenticate(authentication);

		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		log.debug("AuthenticationFilter - successfulAuthentication");
		byte[] tokenSecretBytes = Base64.getEncoder().encode(WebSecurityConfig.TOKEN_SECRET.getBytes());
		SecretKey secretKey = new SecretKeySpec(tokenSecretBytes, SignatureAlgorithm.HS512.getJcaName());
		Instant now = Instant.now();
		String userId = ((User) authResult.getPrincipal()).getUserId();
		String token = Jwts.builder().setSubject(userId)
				.setExpiration(Date.from(now.plusMillis(3600_000L)))
				.setIssuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();
		response.addHeader(HttpHeaders.AUTHORIZATION, WebSecurityConfig.TOKEN_PREFIX + token);
		response.addHeader("UserId", userId);
	}

}
