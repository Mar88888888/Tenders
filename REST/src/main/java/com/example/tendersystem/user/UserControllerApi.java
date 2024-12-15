package com.example.tendersystem.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tendersystem.user.dto.CreateUserDto;
import com.example.tendersystem.user.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Management", description = "Operations related to user management")
public interface UserControllerApi {

    @Operation(summary = "Sign up a new user", description = "Create a new user account and return the user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "Username already taken", content = @Content)
    })
    @PostMapping("/signup")
    ResponseEntity<UserResponseDto> signupSubmit(
            @RequestBody @Parameter(description = "User details to be created", required = true) CreateUserDto user);

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
    })
    @GetMapping
    ResponseEntity<List<UserResponseDto>> getAllUsers();

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<UserResponseDto> getUserById(
            @PathVariable @Parameter(description = "ID of the user to retrieve", required = true) Long id);
}
