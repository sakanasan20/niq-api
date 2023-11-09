package tw.niq.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.niq.api.domain.Authority;
import tw.niq.api.domain.Role;
import tw.niq.api.domain.User;
import tw.niq.api.dto.AuthorityDto;
import tw.niq.api.dto.RoleDto;
import tw.niq.api.dto.UserDto;
import tw.niq.api.mapper.CycleAvoidingMappingContext;
import tw.niq.api.mapper.RoleMapper;
import tw.niq.api.repository.AuthorityRepository;
import tw.niq.api.repository.RoleRepository;
import tw.niq.api.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AuthorityRepository authorityRepository;
	private final RoleMapper roleMapper;
	private final CycleAvoidingMappingContext context;
	
	@Override
	public Collection<RoleDto> getAll(int page, int limit) {
		if (page > 0) page = page - 1;
		Pageable pageable = PageRequest.of(page, limit);
		Collection<RoleDto> roles = roleRepository.findAll(pageable).get()
				.map(role -> roleMapper.toRoleDto(role, context))
				.collect(Collectors.toSet());
		return roles;
	}

	@Override
	public RoleDto getByName(String name) {
		return roleRepository.findByName(name)
				.map(role -> roleMapper.toRoleDto(role, context))
				.orElseThrow(RuntimeException::new);
	}

	@Transactional
	@Override
	public RoleDto createOrUpdate(RoleDto roleDto) {
		if (roleDto.getId() == null) {
			// Create
			return roleMapper.toRoleDto(roleRepository.save(roleMapper.toRole(roleDto, context)), context);
		} else {
			// Update
			Role role = roleRepository.findById(roleDto.getId()).orElseThrow(RuntimeException::new);
			role.setName(roleDto.getName());
			Role roleSaved = roleRepository.save(role);
			return roleMapper.toRoleDto(roleSaved, context);
		}
	}

	@Transactional
	@Override
	public void delete(String name) {
		Role role = roleRepository.findByName(name).orElseThrow(RuntimeException::new);
		Set<User> users = role.getUsers();
		for (User user : users) {
			user.removeRole(role);
			userRepository.save(user);
		}
		roleRepository.deleteByName(name);
	}

	@Override
	public RoleDto addUsers(RoleDto roleDto, List<UserDto> userDtos) {
		Role role = roleRepository.findById(roleDto.getId()).orElseThrow(RuntimeException::new);
		List<User> users = userDtos.stream()
				.map(userDto -> userRepository.findByUserId(userDto.getUserId()).orElseThrow(RuntimeException::new))
				.collect(Collectors.toList());
		for (User user : users) {
			user.addRole(role);
			userRepository.save(user);
		}
		Role roleSaved = roleRepository.save(role);
		return roleMapper.toRoleDto(roleSaved, context);
	}

	@Override
	public RoleDto removeUsers(RoleDto roleDto, List<UserDto> userDtos) {
		Role role = roleRepository.findById(roleDto.getId()).orElseThrow(RuntimeException::new);
		List<User> users = userDtos.stream()
				.map(userDto -> userRepository.findByUserId(userDto.getUserId()).orElseThrow(RuntimeException::new))
				.collect(Collectors.toList());
		for (User user : users) {
			user.removeRole(role);
			userRepository.save(user);
		}
		Role roleSaved = roleRepository.save(role);
		return roleMapper.toRoleDto(roleSaved, context);
	}

	@Override
	public RoleDto addAuthorities(RoleDto roleDto, List<AuthorityDto> authorityDtos) {
		Role role = roleRepository.findById(roleDto.getId()).orElseThrow(RuntimeException::new);
		List<Authority> authorities = authorityDtos.stream()
				.map(authorityDto -> authorityRepository.findByName(authorityDto.getName()).orElseThrow(RuntimeException::new))
				.collect(Collectors.toList());
		for (Authority authority : authorities) {
			role.addAuthority(authority);
			authorityRepository.save(authority);
			roleRepository.save(role);
		}
		Role roleSaved = roleRepository.save(role);
		return roleMapper.toRoleDto(roleSaved, context);
	}

	@Override
	public RoleDto removeAuthorities(RoleDto roleDto, List<AuthorityDto> authorityDtos) {
		Role role = roleRepository.findById(roleDto.getId()).orElseThrow(RuntimeException::new);
		List<Authority> authorities = authorityDtos.stream()
				.map(authorityDto -> authorityRepository.findByName(authorityDto.getName()).orElseThrow(RuntimeException::new))
				.collect(Collectors.toList());
		for (Authority authority : authorities) {
			role.removeAuthority(authority);
			authorityRepository.save(authority);
			roleRepository.save(role);
		}
		Role roleSaved = roleRepository.save(role);
		return roleMapper.toRoleDto(roleSaved, context);
	}

}
