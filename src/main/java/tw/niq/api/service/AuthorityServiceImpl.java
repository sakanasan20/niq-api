package tw.niq.api.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.niq.api.domain.Authority;
import tw.niq.api.domain.Role;
import tw.niq.api.dto.AuthorityDto;
import tw.niq.api.mapper.AuthorityMapper;
import tw.niq.api.repository.AuthorityRepository;
import tw.niq.api.repository.RoleRepository;

@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	private final RoleRepository roleRepository;
	private final AuthorityRepository authorityRepository;
	private final AuthorityMapper authorityMapper;
	
	@Override
	public Collection<AuthorityDto> getAll(int page, int limit) {
		if (page > 0) page = page - 1;
		Pageable pageable = PageRequest.of(page, limit);
		Collection<AuthorityDto> authorities = authorityRepository.findAll(pageable).get()
				.map(authorityMapper::toAuthorityDto)
				.collect(Collectors.toSet());
		return authorities;
	}
	
	@Override
	public Collection<AuthorityDto> getAll() {
		Collection<AuthorityDto> authorities = authorityRepository.findAll().stream()
				.map(authorityMapper::toAuthorityDto)
				.collect(Collectors.toSet());
		return authorities;
	}

	@Override
	public AuthorityDto getByName(String name) {
		return authorityRepository.findByName(name)
				.map(authorityMapper::toAuthorityDto)
				.orElseThrow(RuntimeException::new);
	}

	@Transactional
	@Override
	public AuthorityDto createOrUpdate(AuthorityDto authorityDto) {
		if (authorityDto.getId() == null) {
			// Create
			return authorityMapper.toAuthorityDto(authorityRepository.save(authorityMapper.toAuthority(authorityDto)));
		} else {
			// Update
			Authority authority = authorityRepository.findById(authorityDto.getId()).orElseThrow(RuntimeException::new);
			authority.setName(authorityDto.getName());
			return authorityMapper.toAuthorityDto(authorityRepository.save(authority));
		}
	}

	@Transactional
	@Override
	public void delete(String name) {
		Authority authority = authorityRepository.findByName(name).orElseThrow(RuntimeException::new);
		Set<Role> roles = authority.getRoles();
		for (Role role : roles) {
			role.removeAuthority(authority);
			roleRepository.save(role);
		}
		authorityRepository.deleteByName(name);		
	}

}
