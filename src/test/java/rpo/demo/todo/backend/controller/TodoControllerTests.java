package rpo.demo.todo.backend.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.exception.TodoNotFoundException;
import rpo.demo.todo.backend.service.TodoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoControllerTests {
    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private Map<Long, Todo> todos;

    @BeforeEach
    public void init() {
        this.todos = new HashMap();

        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Todo 1");
        todo1.setStatus(true);
        todos.put(1L, todo1);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Todo 2");
        todo2.setStatus(false);
        todos.put(2L, todo2);

        Todo todo3 = new Todo();
        todo3.setId(3L);
        todo3.setTitle("Todo 3");
        todo3.setStatus(true);
        todos.put(3L, todo3);
    }

    @Test
    public void getAllTest() {
        when(todoService.getAll()).thenReturn(todos.values());

        ResponseEntity<Iterable<Todo>> results = this.todoController.getAll();

        Assertions.assertThat(results.getBody()).isNotNull();

        int nbItemsInBody = 0;
        for(Todo todo : results.getBody()) {
            Assertions.assertThat(todo.getTitle()).isEqualTo(this.todos.get(todo.getId()).getTitle());
            Assertions.assertThat(todo.getStatus()).isEqualTo(this.todos.get(todo.getId()).getStatus());

            nbItemsInBody++;
        }
        Assertions.assertThat(nbItemsInBody).isEqualTo(this.todos.size());
    }

    @Test
    public void getTest() {
        when(this.todoService.getTodo(any(Long.class))).thenAnswer(invocation -> {
            Long argument = (Long) invocation.getArguments()[0];

            if(this.todos.containsKey(argument)) {
                return this.todos.get(argument);
            }
            else {
                throw new TodoNotFoundException();
            }
        });

        for(long i = 1L; i <= 3L; i++) {
            ResponseEntity<Todo> response = this.todoController.get(i);

            Assertions.assertThat(response.getBody()).isNotNull();

            Todo todo = response.getBody();
            Assertions.assertThat(todo.getId()).isEqualTo(i);
            Assertions.assertThat(todo.getTitle()).isEqualTo(this.todos.get(i).getTitle());
            Assertions.assertThat(todo.getStatus()).isEqualTo(this.todos.get(i).getStatus());
        }

        ResponseEntity<Todo> responseForUnexistingTodoId = this.todoController.get(4L);

        Assertions.assertThat(responseForUnexistingTodoId.getBody()).isNull();
        Assertions.assertThat(responseForUnexistingTodoId.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    public void updateTest() {
        Todo todo = new Todo();
        todo.setStatus(true);

        when(this.todoService.getTodo(any(Long.class))).thenAnswer(invocation -> {
            Long id = (Long) invocation.getArguments()[0];

            if(this.todos.containsKey(id)) {
                return this.todos.get(id);
            }
            else {
                throw new TodoNotFoundException();
            }
        });

        when(this.todoService.updateTodo(any(Todo.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Todo arg = ((Todo) args[0]);

            Todo entityToModify = this.todos.get(arg.getId());
            entityToModify.setStatus(arg.getStatus());
            entityToModify.setTitle(arg.getTitle());

            return entityToModify;
        });

        ResponseEntity<Todo> response = this.todoController.update(2L, todo);

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(2L);
        Assertions.assertThat(response.getBody().getStatus()).isTrue();
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo("Todo 2");
    }
}
