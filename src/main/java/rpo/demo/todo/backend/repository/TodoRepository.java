package rpo.demo.todo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rpo.demo.todo.backend.entity.Todo;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {
}
