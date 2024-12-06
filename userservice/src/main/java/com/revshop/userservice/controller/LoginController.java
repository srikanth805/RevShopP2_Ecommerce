package com.revshop.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revshop.userservice.dto.LoginResponse;
import com.revshop.userservice.entity.LoginModel;
import com.revshop.userservice.entity.SellerModel;
import com.revshop.userservice.entity.UserModel;
import com.revshop.userservice.exceptions.InvalidPasswordException;
import com.revshop.userservice.exceptions.SellerNotFoundException;
import com.revshop.userservice.exceptions.UserNotFoundException;
import com.revshop.userservice.service.UserService;

@RestController
@RequestMapping
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@PostMapping("/api/v1/login")
	public ResponseEntity<?> processLogin(@RequestBody LoginModel loginModel) {
		logger.info("Processing login for email: {}", loginModel.getUserEmail());

		UserModel user = userService.findByEmail(loginModel.getUserEmail());
		LoginResponse res = new LoginResponse();

		if (user == null) {
			logger.warn("User not found with email: {}", loginModel.getUserEmail());
			throw new UserNotFoundException("User not found with email: " + loginModel.getUserEmail());
		} else if (!userService.isPasswordCorrect(user, loginModel.getUserPassword())) {
			logger.warn("Incorrect password entered for email: {}", loginModel.getUserEmail());
			throw new InvalidPasswordException("Incorrect password entered by user");
		}

		Integer fetchedUserId = user.getUserId();
		res.setId(fetchedUserId);
		res.setRole(user.getUserRole());

		if ("buyer".equals(user.getUserRole())) {
			logger.info("User logged in as buyer: {}", user.getUserEmail());
			return ResponseEntity.ok(res);
		} else if ("seller".equals(user.getUserRole())) {
			SellerModel seller = userService.getSellerId(fetchedUserId);
			if (seller != null) {
				logger.info("User logged in as seller: {}", user.getUserEmail());
				return ResponseEntity.ok(res);
			} else {
				logger.warn("No seller account found for user: {}", user.getUserEmail());
				throw new SellerNotFoundException("No seller account found for this user.");
			}
		} else {
			logger.warn("Unknown user role for email: {}", loginModel.getUserEmail());
			return ResponseEntity.badRequest().body("Unknown user role.");
		}
	}

	@GetMapping("/api/v1/user")
	public ResponseEntity<?> getUserModel(@RequestParam("userId") Integer userId) {
		logger.info("Fetching user model for userId: {}", userId);
		UserModel userModel = userService.getUserId(userId);
		logger.info("Fetched user model for userId: {}, userModel: {}", userId, userModel);

		return ResponseEntity.ok(userModel);
	}

}