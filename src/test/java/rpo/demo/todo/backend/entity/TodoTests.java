package rpo.demo.todo.backend.entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTests {
    private Todo todo;

    @BeforeEach
    public void initialize() {
        this.todo = new Todo();
    }

    @Test
    public void todoIdTest() {
        this.todo.setId(50L);
        assertEquals(50L, todo.getId(), "TODO's ID has not been set properly, or getId method doesn't work properly.");

        this.todo.setId(3L);
        assertEquals(3L, todo.getId(), "TODO's ID has not been changed properly, or getId method doesn't work properly.");
    }

    @Test
    public void todoTitleTest() {
        this.todo.setTitle("Title");
        assertEquals("Title", todo.getTitle(), "TODO's title has not been set properly, or getTitle method doesn't work properly.");

        this.todo.setTitle("New title");
        assertEquals("New title", todo.getTitle(), "TODO's title has not been modified properly, or getTitle method doesn't work properly.");
    }

    @Test
    public void todoStatusTest() {
        this.todo.setStatus(true);
        assertEquals(true, this.todo.getStatus(), "TODO's status has not been set properly, or getStatus method doesn't work properly.");

        this.todo.setStatus(false);
        assertEquals(false, this.todo.getStatus(), "TODO's status has not been modified properly, or getStatus method doesn't work properly.");
    }
}
