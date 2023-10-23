package rpo.demo.todo.backend.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.exception.TodoNotFoundException;
import rpo.demo.todo.backend.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    public TodoServiceImpl(@Autowired final TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Iterable<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Long addTodo(@NonNull final Todo todo) {
        this.todoRepository.save(todo);

        return todo.getId();
    }

    @Override
    public Todo getTodo(final Long id) {
        return this.todoRepository.findById(id).orElseThrow(TodoNotFoundException::new);
    }

    @Override
    public Todo updateTodo(final Todo todo) {
        return this.todoRepository.save(todo);
    }
}
