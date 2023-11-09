package tw.niq.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBean {
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
		return CustomPasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}
	
	@Bean
	public CustomSimpleUrlAuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler() {
		return new CustomSimpleUrlAuthenticationFailureHandler();
	}
	
}
