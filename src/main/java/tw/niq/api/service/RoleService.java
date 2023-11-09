package tw.niq.api.service;

import java.util.Collection;
import java.util.List;

import tw.niq.api.dto.AuthorityDto;
import tw.niq.api.dto.RoleDto;
import tw.niq.api.dto.UserDto;

public interface RoleService {
	
	Collection<RoleDto> getAll(int page, int limit);

	RoleDto getByName(String name);

	RoleDto createOrUpdate(RoleDto roleDto);

	void delete(String name);

	RoleDto addUsers(RoleDto roleDto, List<UserDto> userDtos);
	
	RoleDto removeUsers(RoleDto roleDto, List<UserDto> userDtos);

	RoleDto addAuthorities(RoleDto roleDto, List<AuthorityDto> authorityDtos);

	RoleDto removeAuthorities(RoleDto roleDto, List<AuthorityDto> authorityDtos);

}
