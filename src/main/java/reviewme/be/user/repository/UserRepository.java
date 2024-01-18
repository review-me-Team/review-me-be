package reviewme.be.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findById(long id);
}
