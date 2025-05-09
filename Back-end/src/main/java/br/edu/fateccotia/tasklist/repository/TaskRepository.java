package br.edu.fateccotia.tasklist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.edu.fateccotia.tasklist.model.Task;
import br.edu.fateccotia.tasklist.model.User;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

	List<Task> findByUser(User user);

	List<Task> findByUserAndDescriptionStartingWithIgnoreCase(User user, String query);


}
