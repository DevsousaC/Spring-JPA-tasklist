package br.edu.fateccotia.tasklist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fateccotia.tasklist.model.Task;
import br.edu.fateccotia.tasklist.model.User;
import br.edu.fateccotia.tasklist.service.AuthService;
import br.edu.fateccotia.tasklist.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;
	@Autowired
	private AuthService authService;

	@PostMapping
	public ResponseEntity<Task> create(@RequestBody Task task,
			@RequestHeader(name = "token", required = true) String token) {
		Boolean isValid = authService.validade(token);
		if (isValid) {
			User user = authService.toUser(token);
			task.setUser(user);
			Task save = taskService.save(task);
			return ResponseEntity.ok(save);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}

	@GetMapping()
	public ResponseEntity<List<Task>> findAll(@RequestHeader(name = "token", required = true) String token) {
		Boolean isValid = authService.validade(token);
		if (isValid) {
			User user = authService.toUser(token);
			List<Task> list = taskService.findByUser(user);
			return ResponseEntity.ok(list);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Task>> search(@RequestHeader(name = "token", required = true) String token,
			@RequestParam(name="q") String query) {
		Boolean isValid = authService.validade(token);
		if (isValid) {
			User user = authService.toUser(token);
			List<Task> list = taskService.search(user, query);
			return ResponseEntity.ok(list);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Task> update(@RequestHeader(name = "token", required = true)	String token, 
			@PathVariable(name="id") Integer id, 
			@RequestBody Task task){
		Boolean isValid = authService.validade(token);
		if (isValid) {
			Task saved = taskService.update(id, task);
			return ResponseEntity.ok(saved);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
