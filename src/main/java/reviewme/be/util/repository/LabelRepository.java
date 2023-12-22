package reviewme.be.util.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.util.entity.Label;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Long> {

    List<Label> findByResumeIsNull();
}
