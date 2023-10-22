package rpo.demo.todo.backend.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpo.demo.todo.backend.entity.Todo;
import rpo.demo.todo.backend.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTests {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    public void addTodoTest() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Title");
        todo.setStatus(true);

        when(this.todoRepository.save(any(Todo.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Todo arg = ((Todo) args[0]);
            arg.setId(1L);

            return arg;
        });

        Todo sentTodo = new Todo();
        sentTodo.setTitle("Title");
        sentTodo.setStatus(true);

        Long result = this.todoService.addTodo(sentTodo);

        Assertions.assertThat(result).isEqualTo(1L);
    }

    @Test
    public void getAllTest() {
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

        when(this.todoRepository.findAll()).thenReturn(todos);

        Iterable<Todo> returnedResult = this.todoService.getAll();
        int nbTodos = 0;
        for(Todo todo : returnedResult) {
            nbTodos++;
        }

        Assertions.assertThat(nbTodos).isEqualTo(3);
    }
}
