package rpo.demo.todo.backend.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.repository.TodoRepository;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTests {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Map<Long, Todo> todos;

    @BeforeEach
    public void init() {
        this.todos = new HashMap<>();

        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Todo 1");
        todo1.setStatus(true);
        this.todos.put(1L, todo1);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Todo 2");
        todo2.setStatus(false);
        this.todos.put(2L, todo2);

        Todo todo3 = new Todo();
        todo3.setId(3L);
        todo3.setTitle("Todo 3");
        todo3.setStatus(true);
        this.todos.put(3L, todo3);
    }

    @Test
    public void addTodoTest() {
        when(this.todoRepository.save(any(Todo.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Todo arg = ((Todo) args[0]);

            arg.setId(4L);
            this.todos.put(4L, arg);

            return arg;
        });

        Todo sentTodo = new Todo();
        sentTodo.setTitle("Title");
        sentTodo.setStatus(true);

        Long result = this.todoService.addTodo(sentTodo);

        Assertions.assertThat(result).isEqualTo(4L);
    }

    @Test
    public void getAllTest() {
        when(this.todoRepository.findAll()).thenReturn(todos.values());

        Iterable<Todo> returnedResult = this.todoService.getAll();
        int nbTodos = 0;
        for(Todo todo : returnedResult) {
            nbTodos++;
        }

        Assertions.assertThat(nbTodos).isEqualTo(3);
    }

    @Test
    public void getTodoTest() {
        when(this.todoRepository.findById(any(Long.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Long arg = ((Long) args[0]);

            return (this.todos.containsKey(arg)) ? Optional.of(this.todos.get(arg)) : Optional.empty();
        });

        for(long i = 1L; i <= 3L; i++) {
            Assertions.assertThat(this.todoService.getTodo(i)).isNotNull();
            Assertions.assertThat(this.todoService.getTodo(i).getId()).isEqualTo(i);
            Assertions.assertThat(this.todoService.getTodo(i).getTitle()).isEqualTo(this.todos.get(i).getTitle());
            Assertions.assertThat(this.todoService.getTodo(i).getStatus()).isEqualTo(this.todos.get(i).getStatus());
        }
    }
}
