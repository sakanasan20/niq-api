package tw.niq.api.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import tw.niq.api.domain.Role;
import tw.niq.api.dto.RoleDto;
import tw.niq.api.model.RoleModel;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RoleMapper extends BaseMapper {
	
	RoleModel toRoleModel(RoleDto roleDto, @Context CycleAvoidingMappingContext context);
	
	RoleDto toRoleDto(RoleModel roleModel, @Context CycleAvoidingMappingContext context);
	
	RoleDto toRoleDto(Role role, @Context CycleAvoidingMappingContext context);
	
	Role toRole(RoleDto roleDto, @Context CycleAvoidingMappingContext context);
	
}
