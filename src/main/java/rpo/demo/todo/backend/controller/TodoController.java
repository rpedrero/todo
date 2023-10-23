package rpo.demo.todo.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.Validation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.exception.TodoNotFoundException;
import rpo.demo.todo.backend.service.TodoService;

@Log4j2
@RequestMapping("/api/todo/")
@RestController
public class TodoController {
    private final TodoService todoService;

    public TodoController(@Autowired final TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Todo>> getAll() {
        return new ResponseEntity<>(this.todoService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Todo> get(@PathVariable final Long id) {
        try {
            return new ResponseEntity<>(this.todoService.getTodo(id), HttpStatus.OK);
        } catch(TodoNotFoundException e) {
            log.error(e);

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Todo> create(@Valid @RequestBody final Todo todo) {
        this.todoService.addTodo(todo);

        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

    @PatchMapping(path = "{id}", consumes = "application/json-patch+json", produces = "application/json")
    public ResponseEntity<Todo> update(@PathVariable final Long id, @RequestBody final Todo todo) {
        Todo todoToBeEdited;

        try {
            todoToBeEdited = this.todoService.getTodo(id);
        } catch(TodoNotFoundException e) {
            log.error(e);

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //If the request body defines the ID differently than in the path variable, a "Bad request" response is sent to the user.
        if(todo.getId() != null && !todo.getId().equals(todoToBeEdited.getId())) {
            log.error("Request rejected because Todo ID incorrect.");

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if(todo.getTitle() != null && !todo.getTitle().isEmpty()) {
            todoToBeEdited.setTitle(todo.getTitle());
        }

        if(todo.getStatus() != todoToBeEdited.getStatus()) {
            todoToBeEdited.setStatus(todo.getStatus());
        }

        try {
            return new ResponseEntity<>(this.todoService.updateTodo(todoToBeEdited), HttpStatus.OK);
        } catch(Exception e) {
            log.error(e);

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
