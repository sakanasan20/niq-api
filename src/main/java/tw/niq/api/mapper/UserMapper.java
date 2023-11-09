package tw.niq.api.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import tw.niq.api.domain.User;
import tw.niq.api.dto.UserDto;
import tw.niq.api.model.UserModel;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper extends BaseMapper {

	UserModel toUserModel(UserDto userDto, @Context CycleAvoidingMappingContext context);
	
	UserDto toUserDto(UserModel userModel, @Context CycleAvoidingMappingContext context);
	
	UserDto toUserDto(User user, @Context CycleAvoidingMappingContext context);
	
	User toUser(UserDto userDto, @Context CycleAvoidingMappingContext context);
	
}
