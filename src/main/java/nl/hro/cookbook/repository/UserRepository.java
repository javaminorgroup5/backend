package nl.hro.cookbook.repository;

import nl.hro.cookbook.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
<<<<<<< HEAD
}
=======
}
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
