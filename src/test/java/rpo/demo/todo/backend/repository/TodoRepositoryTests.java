package rpo.demo.todo.backend.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rpo.demo.todo.backend.entity.Todo;

import java.util.NoSuchElementException;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;

    private Todo todo1;
    private Todo todo2;
    private Todo todo3;
    private Todo todo4;

    @BeforeEach
    public void init() {
        todo1 = new Todo();
        todo1.setTitle("First todo");
        todo1.setStatus(true);
        todo1 = todoRepository.save(todo1);

        todo2 = new Todo();
        todo2.setTitle("Second todo");
        todo2.setStatus(false);
        todo2 = todoRepository.save(todo2);

        todo3 = new Todo();
        todo3.setTitle("Third todo");
        todo3.setStatus(false);
        todo3 = todoRepository.save(todo3);

        todo4 = new Todo();
        todo4.setTitle("Fourth todo");
        todo4.setStatus(true);
        todo4 = todoRepository.save(todo4);
    }

    @Test
    public void saveNewTodoTest() {
        Todo todo = new Todo();
        todo.setTitle("Todo's title");
        todo.setStatus(true);

        //Saving the entity to database.
        todo = todoRepository.save(todo);

        //Checking whether the entity now has a proper ID.
        Assertions.assertThat(todo.getId()).isNotNull();
        Assertions.assertThat(todo.getId()).isGreaterThan(0);

        //Checking whether entity has the same both title and status than before persistence in DB.
        Assertions.assertThat(todo.getTitle()).isEqualTo("Todo's title");
        Assertions.assertThat(todo.getStatus()).isTrue();

        //Checking whether the new entity exists database, with the same data.
        Todo retrievedTodo = todoRepository.findById(todo.getId()).get();
        Assertions.assertThat(retrievedTodo.getId()).isEqualTo(todo.getId());
        Assertions.assertThat(retrievedTodo.getTitle()).isEqualTo(todo.getTitle());
        Assertions.assertThat(retrievedTodo.getStatus()).isEqualTo(todo.getStatus());
    }

    @Test
    public void retrieveAnExistingTodoFromDatabaseWithFindByIdTest() {
        try {
            Todo firstTodo = this.todoRepository.findById(this.todo1.getId()).get();

            Assertions.assertThat(firstTodo.getId()).isEqualTo(todo1.getId());
            Assertions.assertThat(firstTodo.getTitle()).isEqualTo(todo1.getTitle());
            Assertions.assertThat(firstTodo.getStatus()).isEqualTo(todo1.getStatus());
        } catch(NoSuchElementException e) {
            Assertions.fail("First todo could not be retrieved from database");
        }

        try {
            Todo secondTodo = this.todoRepository.findById(this.todo2.getId()).get();

            Assertions.assertThat(secondTodo.getId()).isEqualTo(todo2.getId());
            Assertions.assertThat(secondTodo.getTitle()).isEqualTo(todo2.getTitle());
            Assertions.assertThat(secondTodo.getStatus()).isEqualTo(todo2.getStatus());
        } catch(NoSuchElementException e) {
            Assertions.fail("Second todo could not be retrieved from database");
        }

        try {
            Todo thirdTodo = this.todoRepository.findById(this.todo3.getId()).get();

            Assertions.assertThat(thirdTodo.getId()).isEqualTo(todo3.getId());
            Assertions.assertThat(thirdTodo.getTitle()).isEqualTo(todo3.getTitle());
            Assertions.assertThat(thirdTodo.getStatus()).isEqualTo(todo3.getStatus());
        } catch(NoSuchElementException e) {
            Assertions.fail("Third todo could not be retrieved from database");
        }

        try {
            Todo fourthTodo = this.todoRepository.findById(this.todo4.getId()).get();

            Assertions.assertThat(fourthTodo.getId()).isEqualTo(todo4.getId());
            Assertions.assertThat(fourthTodo.getTitle()).isEqualTo(todo4.getTitle());
            Assertions.assertThat(fourthTodo.getStatus()).isEqualTo(todo4.getStatus());
        } catch(NoSuchElementException e) {
            Assertions.fail("Fourth todo could not be retrieved from database");
        }
    }

    @Test
    public void findAllTest() {
        Iterable<Todo> todos = this.todoRepository.findAll();

        int nbTodos = 0;
        for(Todo todo : todos) {
            nbTodos++;
        }

        Assertions.assertThat(nbTodos).isEqualTo(4);
    }

    @Test
    public void modifyAnExistingTodoAndSaveTest() {
        Todo todo = this.todoRepository.findById(todo1.getId()).get();

        todo.setTitle("First todo's modified title");
        todo.setStatus(false);

        todo = this.todoRepository.save(todo);

        Assertions.assertThat(todo.getTitle()).isEqualTo("First todo's modified title");
        Assertions.assertThat(todo.getStatus()).isFalse();
    }

    @Test
    public void deleteAnExistingTodoAndSaveTest() {
        Todo todo = new Todo();
        todo.setTitle("New todo");
        todo.setStatus(true);

        todo = this.todoRepository.save(todo);

        Long todoId = todo.getId();

        this.todoRepository.delete(todo);

        Optional<Todo> retrievedTodo = this.todoRepository.findById(todoId);

        Assertions.assertThat(retrievedTodo.isEmpty()).isTrue();
    }
}
