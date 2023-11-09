package tw.niq.api.config;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tw.niq.api.NiqApiApplication;
import tw.niq.api.domain.User;
import tw.niq.api.repository.UserRepository;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("AuthorizationFilter - doFilterInternal");
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || !authorizationHeader.startsWith(WebSecurityConfig.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		String tokenToVerify = authorizationHeader.replace(WebSecurityConfig.TOKEN_PREFIX, "");

		UsernamePasswordAuthenticationToken authentication = getAuthentication(tokenToVerify);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String tokenToVerify) {

		UserRepository userRepository = (UserRepository) NiqApiApplication.CTX.getBean(UserRepository.class);

		byte[] tokenSecretBytes = Base64.getEncoder().encode(WebSecurityConfig.TOKEN_SECRET.getBytes());
		SecretKey secretKey = new SecretKeySpec(tokenSecretBytes, SignatureAlgorithm.HS512.getJcaName());

		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		Jwt<Header, Claims> jwt = jwtParser.parse(tokenToVerify);
		String userId = jwt.getBody().getSubject();

		if (userId == null) {
			return null;
		}

		User user = userRepository.findByUserId(userId).orElse(null);

		if (user == null) {
			return null;
		}

		return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
	}

}
