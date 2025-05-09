package br.edu.fateccotia.tasklist.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.edu.fateccotia.tasklist.model.Token;
import br.edu.fateccotia.tasklist.model.User;
import br.edu.fateccotia.tasklist.repository.TokenRepository;
import br.edu.fateccotia.tasklist.repository.UserRepository;

@Service
public class AuthService {
	@Autowired
	private UserRepository ur;
	@Autowired
	private TokenRepository tknRepository;
	private Integer TOKEN_TTL = 60 * 2;

	public void signup(String email, String password) throws Exception {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		Optional<User> userFound = ur.findByEmail(email);
		if (userFound.isPresent()) {
			throw new Exception("Email already exist");
		}

		ur.save(user);
	}

	public Token signin(String email, String password) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		Optional<User> userFound = ur.findByEmail(email);
		if (userFound.isPresent() && userFound.get().getPassword().equals(password)) {
			Token tkn = new Token();
			tkn.setUser(userFound.get());
			tkn.setToken(UUID.randomUUID().toString()); // TODO criar logica tkn
			tkn.setExpTime(Instant.now().plusSeconds(TOKEN_TTL).toEpochMilli());
			tkn = tknRepository.save(tkn);
			return tkn;
		}
		return null;
	}

	public Boolean validade(String tkn) {
		// Resgatar do banco de dados o tkn Bytoken
		// se existir no banco & a expiração é futura
		//então é validade, senão, não é valido	
		Optional<Token> found = tknRepository.findByToken(tkn);
		return found.isPresent() && found.get().getExpTime() 
				>Instant.now().toEpochMilli();
		
	}

	public void signout(String token) {
		Optional<Token> found = tknRepository.findByToken(token);
		found.ifPresent(t -> {t.setExpTime(Instant.now().toEpochMilli());
			tknRepository.save(t);
		});
	}
	
	public User toUser(String token) {
		Optional<Token> found = tknRepository.findByToken(token);
		return found.isPresent() ? found.get().getUser() : null;
	}

}
