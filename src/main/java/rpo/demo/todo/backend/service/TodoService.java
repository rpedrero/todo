package rpo.demo.todo.backend.service;

import lombok.NonNull;
import rpo.demo.todo.backend.entity.Todo;

public interface TodoService {
    Iterable<Todo> getAll();
    Long addTodo(final Todo todo);
    Todo getTodo(final Long id);
    Todo updateTodo(final Todo todo);
}
