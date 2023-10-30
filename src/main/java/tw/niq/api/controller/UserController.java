package tw.niq.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	public static final String PATH_USER_ID = "/api/v1/users/{userId}";
	
	@GetMapping
	public String getUsers() {
		return "GET";
	}
	
	@GetMapping(path = PATH_USER_ID)
	public String getUserByUserId(@RequestParam("userId") String userId) {
		return "GET: " + userId;
	}
	
	@PostMapping
	public String createUser(@RequestBody UserModel userModel) {
		return "POST: " + userModel;
	}
	
	@PutMapping(path = PATH_USER_ID)
	public String updateUserByUserId(@RequestParam("userId") String userId, @RequestBody UserModel userModel) {
		return "PUT: " + userId + ", " + userModel;
	}
	
	@DeleteMapping(path = PATH_USER_ID)
	public String deleteUserByUserId(@RequestParam("userId") String userId) {
		return "DELETE: " + userId;
	}
	
}
