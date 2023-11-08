package tw.niq.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.niq.api.model.UserModel;

@RestController
@RequestMapping(path = UserController.PATH)
public class UserController {

	public static final String PATH = "/api/v1/users";
	public static final String PATH_USER_ID = "/{userId}";
	
	@GetMapping
	public String getUsers(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		return String.format("GET - page: %s, limit: %s", page, limit);
	}
	
	@GetMapping(path = PATH_USER_ID)
	public String getUserByUserId(@PathVariable("userId") String userId) {
		return String.format("GET - userId: %s", userId);
	}
	
	@PostMapping
	public String createUser(@RequestBody UserModel userModel) {
		return String.format("POST - userModel: %s", userModel);
	}
	
	@PutMapping(path = PATH_USER_ID)
	public String updateUserByUserId(@PathVariable("userId") String userId, @RequestBody UserModel userModel) {
		return String.format("PUT - userId: %s, userModel: %s", userId, userModel);
	}
	
	@DeleteMapping(path = PATH_USER_ID)
	public String deleteUserByUserId(@PathVariable("userId") String userId) {
		return String.format("DELETE - userId: %s", userId);
	}
	
}
