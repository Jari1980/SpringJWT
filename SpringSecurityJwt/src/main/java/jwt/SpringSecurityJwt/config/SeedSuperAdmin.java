package jwt.SpringSecurityJwt.config;


import jwt.SpringSecurityJwt.entity.User;
import jwt.SpringSecurityJwt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedSuperAdmin {
    @Bean
    CommandLineRunner seedSuperAdminSeeder(UserRepository userRepository,
                                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("superadmin")) {
                String hashed = passwordEncoder.encode("admin123");
                User admin = User.builder()
                        .username("superadmin")
                        .password(hashed)
                        .role("ADMIN")
                        .build();
                userRepository.save(admin);
                System.out.println("Superadmin seeded!");
            }
        };
    }
}
