package com.catinasw.T03.domain.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private List<String> roles;

    public User() {}

    public User(String login, String password, List<String> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
