package com.spring.todomanagement.domain.user;

import com.spring.todomanagement.domain.todo.Todo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Todo> todos = new ArrayList<>();

    @Builder
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
        todo.setUser(this);
    }
}
