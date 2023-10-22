package rpo.demo.todo.backend.bootstrap;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import rpo.demo.todo.backend.domain.entity.Todo;
import rpo.demo.todo.backend.domain.enums.TodoStatus;
import rpo.demo.todo.backend.service.TodoService;

@Log4j2
@Service
public class Bootstrap implements CommandLineRunner {
    private final TodoService todoService;

    public Bootstrap(@Autowired final TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public void run(String... args) throws Exception {
        final Todo todo1 = new Todo();
        todo1.setTitle("Write a specifications document");
        todo1.setStatus(TodoStatus.DONE);
        this.todoService.addTodo(todo1);
        log.debug("First todo created.");

        final Todo todo2 = new Todo();
        todo2.setTitle("Develop features");
        todo2.setStatus(TodoStatus.DOING);
        this.todoService.addTodo(todo2);
        log.debug("Second todo created.");

        final Todo todo3 = new Todo();
        todo3.setTitle("Perform functional tests");
        todo3.setStatus(TodoStatus.TODO);
        this.todoService.addTodo(todo3);
        log.debug("Third todo created.");

        final Todo todo4 = new Todo();
        todo4.setTitle("Deliver product");
        todo4.setStatus(TodoStatus.TODO);
        this.todoService.addTodo(todo4);
        log.debug("Fourth todo created.");
    }
}
