package com.example.demo4.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table(name  = "user_table")
public class User {
    @Id
    private String id;
    private String username;
    private String email;

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Other user-related fields

    // Constructors, getters, setters, equals, hashCode, toString provided by Lombok
}