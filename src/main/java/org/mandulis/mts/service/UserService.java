package org.mandulis.mts.service;

import org.mandulis.mts.entity.Group;
import org.mandulis.mts.entity.User;
import org.mandulis.mts.repository.UserRepository;
import org.mandulis.mts.dto.RegistrationRequest;
import org.mandulis.mts.dto.RegistrationResponse;
import org.mandulis.mts.dto.UserResponse;
import org.mandulis.mts.exception.UserValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<RegistrationResponse> register(RegistrationRequest registrationRequest) {
        validateRegistrationRequest(registrationRequest);
        Optional<User> user = userRepository.findByUsernameOrEmail(
                registrationRequest.getUsername(),
                registrationRequest.getEmail()
        );
        if (user.isPresent()) {
            throw new UserValidationException("User with the given username or email already exists");
        }
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setFirstName(registrationRequest.getFirstName());
        newUser.setLastName(registrationRequest.getLastName());
        var savedUser = userRepository.save(newUser);
        return Optional.of(convertEntityToRegistrationResponseDto(savedUser));
    }

    private void validateRegistrationRequest(RegistrationRequest registrationRequest) {
        if (registrationRequest.getUsername() == null || registrationRequest.getUsername().isEmpty()) {
            throw new UserValidationException("Username is mandatory");
        }
        if (registrationRequest.getEmail() == null || registrationRequest.getEmail().isEmpty()) {
            throw new UserValidationException("Email is mandatory");
        }
        if (registrationRequest.getPassword() == null || registrationRequest.getPassword().isEmpty()) {
            throw new UserValidationException("Password is mandatory");
        }
    }

    public RegistrationResponse convertEntityToRegistrationResponseDto(User user) {
        return new RegistrationResponse(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public UserResponse convertEntityToUserResponseDto(User user) {
        UserResponse userDto =  new UserResponse();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setGroups(
            user.getGroups()
                .stream()
                .map(Group::getName)
                .toList()
        );
        userDto.setRole(user.getRole().getName());
        return userDto;
    }

    public Optional<UserResponse> findUserResponseById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.map(this::convertEntityToUserResponseDto);
    }

    public Optional<UserResponse> findUserResponseByUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        return foundUser.map(this::convertEntityToUserResponseDto);
    }
}
