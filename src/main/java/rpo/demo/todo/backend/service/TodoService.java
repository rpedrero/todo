package rpo.demo.todo.backend.service;

import org.springframework.stereotype.Service;
import rpo.demo.todo.backend.repository.TodoRepository;
import rpo.demo.todo.backend.domain.entity.Todo;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(final TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Iterable<Todo> getAll() {
        return todoRepository.findAll();
    }
}
