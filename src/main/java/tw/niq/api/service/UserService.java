package tw.niq.api.service;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetailsService;

import tw.niq.api.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	Collection<UserDto> getAll(int page, int limit);
	
	Collection<UserDto> getAll();
	
	UserDto getByUsername(String username);

	UserDto getByUserId(String userId);

	UserDto createOrUpdate(UserDto userDto);

	void deleteByUserId(String userId);

}
