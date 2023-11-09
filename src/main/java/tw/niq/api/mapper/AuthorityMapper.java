package tw.niq.api.mapper;

import org.mapstruct.Mapper;

import tw.niq.api.domain.Authority;
import tw.niq.api.dto.AuthorityDto;
import tw.niq.api.model.AuthorityModel;

@Mapper
public interface AuthorityMapper extends BaseMapper {

	AuthorityModel toAuthorityModel(AuthorityDto authorityDto);
	
	AuthorityDto toAuthorityDto(AuthorityModel authorityModel);
	
	AuthorityDto toAuthorityDto(Authority authority);
	
	Authority toAuthority(AuthorityDto authorityDto);
	
}
