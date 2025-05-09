package br.edu.fateccotia.tasklist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.fateccotia.tasklist.model.Task;
import br.edu.fateccotia.tasklist.model.User;
import br.edu.fateccotia.tasklist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public Task save(Task task) {
		
		return taskRepository.save(task);
	}
	

	public List<Task> findAll(){
		List<Task> list = new ArrayList<Task>();
		taskRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}


	public List<Task> findByUser(User user) {
		List<Task> list = new ArrayList<Task>();
		taskRepository.findByUser(user).iterator().forEachRemaining(list::add);
		return list;
	}


	public List<Task> search(User user, String query) {
		List<Task> list = new ArrayList<Task>();
		taskRepository.findByUserAndDescriptionStartingWithIgnoreCase(user, query).iterator().forEachRemaining(list::add);
		return list;
	}


	public Task update(Integer id, Task task) {
	    // 1 - Buscar a task existente pelo ID
	    Optional<Task> optionalTask = taskRepository.findById(id);
	    if (!optionalTask.isPresent()) {
	        throw new EntityNotFoundException("A task com o id  " + id + " não foi encontrada.");
	    }

	    Task existingTask = optionalTask.get();

	    // 2 - Atualizar os campos da task
	    existingTask.setDescription(task.getDescription());
	    existingTask.setStatus(task.getStatus());
	    existingTask.setUser(task.getUser());

	    // 3 - Salvar as alterações no banco
	    return taskRepository.save(existingTask);
	}

}
