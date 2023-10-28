package rpo.demo.todo.backend.controller;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.exception.TodoNotFoundException;
import rpo.demo.todo.backend.service.TodoService;

import java.time.LocalDateTime;

@Log4j2
@RequestMapping({"todo", "todo/"})
@RestController
public class TodoController {
    private final TodoService todoService;

    public TodoController(@Autowired final TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable<Todo>> getAll() {
        return new ResponseEntity<>(this.todoService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"{id}", "{id}/"}, produces = "application/json")
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
        todo.setDateDone((todo.getStatus() != null && todo.getStatus()) ? LocalDateTime.now() : null);

        this.todoService.addTodo(todo);

        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

    @PatchMapping(path = {"{id}", "{id}/"}, consumes = "application/json-patch+json", produces = "application/json")
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

        if(todo.getDescription() != null && !todo.getDescription().isEmpty()) {
            todoToBeEdited.setDescription(todo.getDescription());
        }

        if(todo.getStatus() != todoToBeEdited.getStatus()) {
            todoToBeEdited.setStatus(todo.getStatus());

            todoToBeEdited.setDateDone((todo.getStatus()) ? LocalDateTime.now() : null);
        }

        try {
            return new ResponseEntity<>(this.todoService.updateTodo(todoToBeEdited), HttpStatus.OK);
        } catch(Exception e) {
            log.error(e);

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
