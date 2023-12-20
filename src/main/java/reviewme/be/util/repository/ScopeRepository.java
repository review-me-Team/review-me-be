package reviewme.be.util.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.util.entity.Emoji;
import reviewme.be.util.entity.Scope;

import java.util.List;

public interface ScopeRepository extends JpaRepository<Scope, Long> {

    List<Scope> findAll();
}
