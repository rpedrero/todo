package rpo.demo.todo.backend.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    public TodoServiceImpl(@Autowired final TodoRepository todoRepository) {
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
