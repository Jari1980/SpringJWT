package jwt.SpringJwt.repository;

import jwt.SpringJwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
