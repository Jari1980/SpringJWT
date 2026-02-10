package jwt.SpringJwt.service;

import jwt.SpringJwt.config.JwtUtil;
import jwt.SpringJwt.entity.User;
import jwt.SpringJwt.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Register a new user
    public User register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = User.builder()
                .username(username)
                .password(hashed)
                .role("USER")
                .build();

        return userRepository.save(user);
    }

    // Login returns JWT token
    public String login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }

    // Get User object from JWT token
    public User getUserFromToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        String username = jwtUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
