package br.edu.fateccotia.tasklist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fateccotia.tasklist.model.Token;
import br.edu.fateccotia.tasklist.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody Map<String, String> user) {
		String email = user.get("email");
		String password = user.get("password");
		try {
			authService.signup(email, password);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<Token> signin(@RequestBody Map<String, String> user){
		Token tkn = authService.signin(user.get("email"), user.get("password"));
		if(tkn == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(tkn);
	}
	
	@PostMapping("/check")
	public	ResponseEntity<?> check(@RequestHeader String token) {
		Boolean isValid = authService.validade(token);
			return (isValid) ? ResponseEntity.ok().build() 
					: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();	
	}
	
	@PostMapping("/signout")
	public ResponseEntity<?> signout(@RequestHeader String token) {
		authService.signout(token);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
