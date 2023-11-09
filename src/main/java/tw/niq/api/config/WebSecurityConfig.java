package tw.niq.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.api.controller.UserController;
import tw.niq.api.service.UserService;

@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String TOKEN_SECRET = "AaMckyLM973x9qPw9u5G7k7EYxYn0f0jP0FERRbwbsEEUKvpkYWudx57AJRgTbh3";
	public static final String SIGN_UP_URL = UserController.ROOT_PATH;
	public static final String SIGN_IN_URL = "/login";
	
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final CustomSimpleUrlAuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		log.debug("WebSecurityConfig - configure");
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);

		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
		authenticationFilter.setAuthenticationFailureHandler(customSimpleUrlAuthenticationFailureHandler);
		AuthorizationFilter authorizationFilter = new AuthorizationFilter(authenticationManager);

		http.authenticationManager(authenticationManager)
			.addFilter(authenticationFilter)
			.addFilter(authorizationFilter)
			.csrf(csrf -> csrf.disable())
			.sessionManagement(
					(sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
					.requestMatchers(HttpMethod.POST, SIGN_IN_URL).permitAll()
					.requestMatchers("/error").permitAll()
					.anyRequest().authenticated());

		return http.build();
	}

}
