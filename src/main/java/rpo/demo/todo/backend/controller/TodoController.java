package rpo.demo.todo.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.service.TodoService;

@RequestMapping("/api/todo/")
@RestController
public class TodoController {
    private final TodoService todoService;

    public TodoController(@Autowired final TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<Todo>> getAll() {
        return new ResponseEntity<>(this.todoService.getAll(), HttpStatus.CREATED);
    }
}
