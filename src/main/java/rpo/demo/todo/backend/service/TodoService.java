package rpo.demo.todo.backend.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpo.demo.todo.backend.repository.TodoRepository;
import rpo.demo.todo.backend.domain.entity.Todo;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(@Autowired final TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Iterable<Todo> getAll() {
        return todoRepository.findAll();
    }

    public Long addTodo(@NonNull final Todo todo) {
        this.todoRepository.save(todo);

        return todo.getId();
    }
}
