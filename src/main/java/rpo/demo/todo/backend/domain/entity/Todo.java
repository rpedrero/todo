package rpo.demo.todo.backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rpo.demo.todo.backend.domain.enums.TodoStatus;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TodoStatus status;
    @NotEmpty
    @NotBlank
    private String title;
}
