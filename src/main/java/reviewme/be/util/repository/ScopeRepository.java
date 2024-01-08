package reviewme.be.util.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.util.entity.Scope;

import java.util.List;
import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, Long> {

    List<Scope> findAll();

    Optional<Scope> findById(long id);
}
