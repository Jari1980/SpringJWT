package jwt.SpringJwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jwt.SpringJwt.dto.UserResponse;
import jwt.SpringJwt.entity.User;
import jwt.SpringJwt.service.AuthService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    // Register endpoint
    @PostMapping("/register")
    public User register(@RequestBody AuthRequest request) {
        return authService.register(request.getUsername(), request.getPassword());
    }

    // Login endpoint
    @Operation(summary = "Login user and receive JWT token")
    @PostMapping("/login")
    public JwtResponse login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return new JwtResponse(token);
    }

    // Get current user from JWT
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public UserResponse me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.replace("Bearer ", "");
        User user = authService.getUserFromToken(token);
        return mapToResponse(user);
    }

    // Helper to map User entity to DTO
    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    // DTOs
    @Data
    static class AuthRequest {
        private String username;
        private String password;
    }

    @Data
    static class JwtResponse {
        private final String token;
    }
}
