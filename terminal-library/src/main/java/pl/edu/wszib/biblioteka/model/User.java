package pl.edu.wszib.biblioteka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int id;
    private String login;
    private String password;
    private String role;
}
