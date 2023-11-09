package tw.niq.api.service;

import java.util.Collection;

import tw.niq.api.dto.AuthorityDto;

public interface AuthorityService {
	
	Collection<AuthorityDto> getAll(int page, int limit);

	Collection<AuthorityDto> getAll();
	
	AuthorityDto getByName(String name);

	AuthorityDto createOrUpdate(AuthorityDto authorityDto);

	void delete(String name);

}
