package rpo.demo.todo.backend.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.service.TodoService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoControllerTests {
    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private List<Todo> todos;

    @BeforeEach
    public void init() {
        List<Todo> todos = new ArrayList<>();

        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Todo 1");
        todo1.setStatus(true);
        todos.add(todo1);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Todo 2");
        todo2.setStatus(false);
        todos.add(todo2);

        Todo todo3 = new Todo();
        todo3.setId(3L);
        todo3.setTitle("Todo 3");
        todo3.setStatus(true);
        todos.add(todo3);

        when(todoService.getAll()).thenReturn(todos);
    }

    @Test
    public void getAllTest() {
        ResponseEntity<Iterable<Todo>> results = this.todoController.getAll();

        for(Todo todo : results.getBody()) {
            if(todo.getId() == 1L) {
                Assertions.assertThat(todo.getTitle()).isEqualTo("Todo 1");
                Assertions.assertThat(todo.getStatus()).isTrue();
            }
            else if(todo.getId() == 2L) {
                Assertions.assertThat(todo.getTitle()).isEqualTo("Todo 2");
                Assertions.assertThat(todo.getStatus()).isFalse();
            }
            else if(todo.getId() == 3L) {
                Assertions.assertThat(todo.getTitle()).isEqualTo("Todo 3");
                Assertions.assertThat(todo.getStatus()).isTrue();
            } else {
                Assertions.fail("At least one item should not be part of the response.");
            }
        }
    }
}
