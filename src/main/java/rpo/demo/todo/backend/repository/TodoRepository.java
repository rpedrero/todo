package rpo.demo.todo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import rpo.demo.todo.backend.domain.entity.Todo;

public interface TodoRepository extends CrudRepository<Todo, Long> {
}
