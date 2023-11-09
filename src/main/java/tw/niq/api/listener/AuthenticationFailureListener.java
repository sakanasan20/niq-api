package tw.niq.api.listener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.api.domain.LoginFailure;
import tw.niq.api.domain.LoginFailure.LoginFailureBuilder;
import tw.niq.api.domain.User;
import tw.niq.api.repository.LoginFailureRepository;
import tw.niq.api.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {
	
	private final UserRepository userRepository;
	private final LoginFailureRepository loginFailureRepository;
	
	@EventListener
	public void listen(AuthenticationFailureBadCredentialsEvent event) {
		
		LoginFailureBuilder loginFailureBuilder = LoginFailure.builder();

		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
			
			if (token.getPrincipal() instanceof String) {
				String username = (String) token.getPrincipal();
				loginFailureBuilder.username(username);
				log.debug("Attempted username: " + username);
				userRepository.findByUsername(username).ifPresent(userEntity -> loginFailureBuilder.user(userEntity));
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
				loginFailureBuilder.sourceIp(details.getRemoteAddress());
				log.debug("Remote Address: " + details.getRemoteAddress());
			}
			
			LoginFailure loginFailureSaved = loginFailureRepository.save(loginFailureBuilder.build());
			
			log.debug("LoginFailure: " + loginFailureSaved.getId());
			
			if (loginFailureSaved.getUser() != null) {
				lockUserAccount(loginFailureSaved.getUser());
			}
		}
	}

	private void lockUserAccount(User user) {
		List<LoginFailure> loginFailures = 
				loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		
		if (loginFailures.size() > 3) {
			log.debug("Locking user: " + user.getUsername());
			user.setAccountNonLocked(false);
			userRepository.save(user);
		}
		
	}

}
