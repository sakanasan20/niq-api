package tw.niq.api.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.api.mapper.CycleAvoidingMappingContext;
import tw.niq.api.mapper.UserMapper;
import tw.niq.api.model.UserModel;
import tw.niq.api.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = UserController.ROOT_PATH)
public class UserController {

	public static final String ROOT_PATH = "/api/v1/users";
	public static final String USER_ID_PATH = "/{userId}";
	
	private final UserService userService;
	private final UserMapper userMapper;
	private final CycleAvoidingMappingContext context;
	private final SessionRegistry sessionRegistry;
	
	@GetMapping
	public Set<UserModel> getAll(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		return userService.getAll(page, limit).stream()
				.map(userDto -> userMapper.toUserModel(userDto, context))
				.collect(Collectors.toSet());
	}

	@GetMapping(
			path = UserController.USER_ID_PATH, 
			produces = { 
					MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE })
	public UserModel getByUserId(@PathVariable("userId") String userId) {
		return userMapper.toUserModel(userService.getByUserId(userId), context);
	}

	@PostMapping(
			consumes = { 
					MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE}, 
			produces = {
					MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE})
	public UserModel createUser(@RequestBody UserModel userModel) {
		return userMapper.toUserModel(userService.createOrUpdate(userMapper.toUserDto(userModel, context)), context);
	}

	@PutMapping(
			path = UserController.USER_ID_PATH, 
			consumes = { 
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE }, 
			produces = { 
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserModel updateUser(@PathVariable("userId") String userId, @RequestBody UserModel userModel) {
		userModel.setUserId(userId);
		return userMapper.toUserModel(userService.createOrUpdate(userMapper.toUserDto(userModel, context)), context);
	}

	@DeleteMapping(path = UserController.USER_ID_PATH)
	public void deleteByUserId(@PathVariable("userId") String userId) {
		userService.deleteByUserId(userId);
	}
	
	@GetMapping("/getAllLoggedIn")
	public Set<UserModel> getAllLoggedIn(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<Object> principals = sessionRegistry.getAllPrincipals();
		return principals.stream().map(principal -> (UserModel) principal).collect(Collectors.toSet());
	}
	
}
