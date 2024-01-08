package reviewme.be.util.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.util.entity.Occupation;

import java.util.List;
import java.util.Optional;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    List<Occupation> findAll();

    Optional<Occupation> findById(long id);
}
