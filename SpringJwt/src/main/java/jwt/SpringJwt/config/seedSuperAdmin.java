package jwt.SpringJwt.config;

import jwt.SpringJwt.entity.User;
import jwt.SpringJwt.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class seedSuperAdmin {
    @Bean
    CommandLineRunner seedSuperAdminSeeder(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByUsername("superadmin")) {
                String hashed = BCrypt.hashpw("1234", BCrypt.gensalt());
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
