package reviewme.be.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.util.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);
}
