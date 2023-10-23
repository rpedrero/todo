package rpo.demo.todo.backend.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.exception.TodoNotFoundException;
import rpo.demo.todo.backend.service.TodoService;
import rpo.demo.todo.backend.util.JsonUtil;
import rpo.demo.todo.backend.util.JsonUtilImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith({MockitoExtension.class})
@Import(JsonUtilImpl.class)
public class TodoControllerTests {
    @MockBean
    private TodoService todoService;

    @Autowired
    private TodoController todoController;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonUtil jsonUtil;

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
    public void getAllTest() throws Exception {
        when(todoService.getAll()).thenReturn(todos.values());

        ResponseEntity<Iterable<Todo>> results = this.todoController.getAll();

        Assertions.assertThat(results.getBody()).isNotNull();

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get("/api/todo/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String todosListAsJson = this.jsonUtil.objectToJson(this.todos.values());
        String receivedResponseAsJson = response.getContentAsString();

        Assertions.assertThat(receivedResponseAsJson).isEqualTo(todosListAsJson);
    }

    @Test
    public void getTest() throws Exception {
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
            MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                    .get("/api/todo/" + i)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            String todoAsAString = this.jsonUtil.objectToJson(this.todos.get(i));

            Assertions.assertThat(response.getContentAsString()).isEqualTo(todoAsAString);
        }

        MockHttpServletResponse responseForUnexistingTodoId = mvc.perform(MockMvcRequestBuilders
                    .get("/api/todo/4")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse();
    }

    @Test
    public void addTest() throws Exception {
        when(this.todoService.getTodo(any(Long.class))).thenAnswer(invocation -> {
            Long argument = (Long) invocation.getArguments()[0];

            if (this.todos.containsKey(argument)) {
                return this.todos.get(argument);
            } else {
                throw new TodoNotFoundException();
            }
        });

        Todo newTodo = new Todo();
        newTodo.setStatus(true);
        newTodo.setTitle("New Todo");

        String newTodoAsAString = this.jsonUtil.objectToJson(newTodo);

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/api/todo/")
                        .contentType("application/json")
                        .content(newTodoAsAString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getContentAsString()).isEqualTo(newTodoAsAString);

        Todo incorrectTodo = new Todo();
        incorrectTodo.setStatus(true);
        incorrectTodo.setTitle("   ");

        String incorrectTodoAsAString = this.jsonUtil.objectToJson(incorrectTodo);

        MockHttpServletResponse responseForIncorrectTodo = mvc.perform(MockMvcRequestBuilders.post("/api/todo/")
                        .contentType("application/json")
                        .content(incorrectTodoAsAString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
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
