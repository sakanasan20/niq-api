package tw.niq.api.bootstrap;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.niq.api.domain.Authority;
import tw.niq.api.domain.Role;
import tw.niq.api.domain.Roles;
import tw.niq.api.domain.User;
import tw.niq.api.repository.AuthorityRepository;
import tw.niq.api.repository.RoleRepository;
import tw.niq.api.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class InitialDataLoader {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {
		createAdmin();
	}
	
	@Transactional
	private void createAdmin() {
		userRepository.findByUsername("admin").orElseGet(() -> {
			Authority authorityRead = createAuthority("read");
			Authority authorityWrite = createAuthority("write");
			Authority authorityDelete = createAuthority("delete");
			Role roleAdmin = createRole(Roles.ADMIN.name(), Arrays.asList(authorityRead, authorityWrite, authorityDelete));
			User admin = createUser("admin", "admin", Arrays.asList(roleAdmin));
			return admin;
		});
	}

	@Transactional
	private User createUser(String username, String password, Collection<Role> roles) {
		User user = User.builder().userId(UUID.randomUUID().toString()).username(username).password(passwordEncoder.encode(password)).roles(roles).build();
		return userRepository.findByUsername(username).orElseGet(() -> userRepository.save(user));
	}

	@Transactional
	private Role createRole(String name, Collection<Authority> authorities) {
		return roleRepository.findByName(name).orElseGet(() -> roleRepository.save(Role.builder().name(name).authorities(authorities).build()));
	}

	@Transactional
	private Authority createAuthority(String name) {
		return authorityRepository.findByName(name).orElseGet(() -> authorityRepository.save(Authority.builder().name(name).build()));
	}

}
