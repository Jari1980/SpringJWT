package jwt.SpringSecurityJwt.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jwt.SpringSecurityJwt.entity.User;
import jwt.SpringSecurityJwt.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inject secret key and token expiration from application.properties
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private final long jwtExpirationMs = 3600_000; // 1 hour

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public User register(@RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        return userRepository.save(user);
    }

    @Operation(summary = "Login user and receive JWT token")
    @PostMapping("/login")
    public JwtResponse login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = generateToken(user);
        return new JwtResponse(token);
    }

    // Helper: generate JWT using secret key
    private String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Operation(summary = "Get current logged-in user", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    // DTOs
    @Data
    public static class AuthRequest {
        private String username;
        private String password;
    }

    @Data
    public static class JwtResponse {
        private final String token;
    }

    @Data
    public static class UserResponse {
        private final Long id;
        private final String username;
        private final String role;
    }
}
