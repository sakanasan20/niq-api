package tw.niq.api.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.api.domain.LoginSuccess;
import tw.niq.api.domain.LoginSuccess.LoginSuccessBuilder;
import tw.niq.api.domain.User;
import tw.niq.api.repository.LoginSuccessRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationSuccessListener {
	
	private final LoginSuccessRepository loginSuccessRepository;

	@EventListener
	public void listen(AuthenticationSuccessEvent event) {
		
		LoginSuccessBuilder loginSuccessBuilder = LoginSuccess.builder();

		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
			log.debug(token.toString());
			
			if (token.getPrincipal() instanceof User) {
				User user = (User) token.getPrincipal();
				loginSuccessBuilder.user(user);
				log.debug("User logged in: " + user.getUsername());
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
				loginSuccessBuilder.sourceIp(details.getRemoteAddress());
				log.debug("Remote Address: " + details.getRemoteAddress());
			}
			
			LoginSuccess loginSuccessSaved = loginSuccessRepository.save(loginSuccessBuilder.build());
			
			log.debug("LoginSuccess: " + loginSuccessSaved.getId());
		}
	}

}
