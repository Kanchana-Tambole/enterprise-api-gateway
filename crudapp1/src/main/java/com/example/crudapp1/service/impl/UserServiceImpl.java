package com.example.crudapp1.service.impl;

import com.example.crudapp1.dto.UserDTO;
import com.example.crudapp1.entity.User;
import com.example.crudapp1.repository.UserRepository;
import com.example.crudapp1.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Maps User entity to UserDTO
    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    // Maps UserDTO to User entity (with password encoding)
    private User mapToEntity(UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodePassword(dto.getPassword()))
                .roles(dto.getRoles())
                .build();
    }

    // Encodes password only if not null or blank
    private String encodePassword(String rawPassword) {
        return (rawPassword != null && !rawPassword.isBlank())
                ? passwordEncoder.encode(rawPassword)
                : null;
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        User user = mapToEntity(dto);
        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // Only update password if provided
        String encodedPassword = encodePassword(dto.getPassword());
        if (encodedPassword != null) {
            user.setPassword(encodedPassword);
        }

        // Only update roles if provided
        if (dto.getRoles() != null) {
            user.setRoles(dto.getRoles());
        }

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

