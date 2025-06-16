package com.example.crudapp1.dto;

public class AuthResponse {
    private String token;

    // Default constructor (required for Jackson)
    public AuthResponse() {
    }

    // Constructor that accepts a JWT token
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter and setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

