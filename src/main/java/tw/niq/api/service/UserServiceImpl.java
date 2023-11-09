package tw.niq.api.service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.niq.api.domain.User;
import tw.niq.api.dto.UserDto;
import tw.niq.api.mapper.CycleAvoidingMappingContext;
import tw.niq.api.mapper.UserMapper;
import tw.niq.api.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final CycleAvoidingMappingContext context;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}
	
	@Override
	public Collection<UserDto> getAll(int page, int limit) {
		if (page > 0) page = page - 1;
		Pageable pageable = PageRequest.of(page, limit);
		Collection<UserDto> users = userRepository.findAll(pageable).get()
				.map(user -> userMapper.toUserDto(user, context))
				.collect(Collectors.toSet());
		return users;
	}

	@Override
	public Collection<UserDto> getAll() {
		Collection<UserDto> users = userRepository.findAll().stream()
				.map(user -> userMapper.toUserDto(user, context))
				.collect(Collectors.toSet());
		return users;
	}

	
	@Override
	public UserDto getByUsername(String username) {
		return userRepository.findByUsername(username)
				.map(user -> userMapper.toUserDto(user, context))
				.orElseThrow(RuntimeException::new);
	}

	@Override
	public UserDto getByUserId(String userId) {
		return userRepository.findByUserId(userId)
				.map(user -> userMapper.toUserDto(user, context))
				.orElseThrow(RuntimeException::new);
	}

	@Transactional
	@Override
	public UserDto createOrUpdate(UserDto userDto) {
		User user;
		if (userDto.getUserId() == null || userDto.getUserId().isBlank()) {
			// Create
			userDto.setUserId(UUID.randomUUID().toString());
			userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user = userMapper.toUser(userDto, context);
		} else {
			// Update
			user = userRepository.findByUserId(userDto.getUserId()).orElseThrow(RuntimeException::new);
			user.setUsername(userDto.getUsername());
			user.setAccountNonExpired(userDto.getAccountNonExpired());
			user.setAccountNonLocked(userDto.getAccountNonLocked());
			user.setCredentialsNonExpired(userDto.getCredentialsNonExpired());
			user.setEnabled(userDto.getEnabled());
		}
		User userSaved = userRepository.save(user);
		return userMapper.toUserDto(userSaved, context);
	}

	@Transactional
	@Override
	public void deleteByUserId(String userId) {
		userRepository.deleteByUserId(userId);
	}

}
