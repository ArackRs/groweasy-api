package com.groweasy.groweasyapi.loginregister.controllers;

import com.groweasy.groweasyapi.loginregister.model.dto.response.UserResponse;
import com.groweasy.groweasyapi.loginregister.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller", description = "API for user operations")
public class UserController {
    private final UserService userService;

    @GetMapping(value = "")
    @Operation(
            summary = "Get all users",
            description = "Fetches all users in the system"
    )
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping(value = "/{username}")
    @Operation(
            summary = "Get a user by username",
            description = "Fetches user details by their username"
    )
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {

        UserResponse userResponse = userService.getUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PutMapping(value = "")
    @Operation(
            summary = "Update user details",
            description = "Updates an existing user's details"
    )
    public ResponseEntity<HashMap<String, String>> updateUsername(@RequestParam Long userId, @RequestParam String username) {
        userService.updateUsername(userId, username);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User updated successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/account")
    @Operation(
            summary = "Delete own account",
            description = "Allows a user to delete their own account"
    )
    public ResponseEntity<Void> deleteAccount(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
