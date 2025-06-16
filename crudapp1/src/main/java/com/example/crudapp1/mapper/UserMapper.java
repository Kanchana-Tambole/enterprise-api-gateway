package com.example.crudapp1.mapper;

import com.example.crudapp1.dto.UserDTO;
import com.example.crudapp1.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(null) // never expose raw password
                .roles(user.getRoles())
                .build();
    }

    public User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .roles(dto.getRoles())
                .build();
    }
}
