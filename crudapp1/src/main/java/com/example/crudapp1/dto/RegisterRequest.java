package com.example.crudapp1.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String name;  // Optional: if you want to store user's name

    // getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

