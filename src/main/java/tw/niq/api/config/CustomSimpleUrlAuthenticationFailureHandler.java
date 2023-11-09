package tw.niq.api.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.debug("CustomSimpleUrlAuthenticationFailureHandler - onAuthenticationFailure");
		log.debug(exception.getMessage());
		response.setHeader("AuthenticationException", exception.getClass().getSimpleName());
		super.onAuthenticationFailure(request, response, exception);
		}
	}
