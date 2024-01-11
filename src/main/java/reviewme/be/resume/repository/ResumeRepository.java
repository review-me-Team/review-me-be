package reviewme.be.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.resume.entity.Resume;


import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByIdAndDeletedAtIsNull(long id);

    Optional<Resume> findByUrlAndDeletedAtIsNull(String url);
    List<Resume> findByUserIdAndDeletedAtIsNull(long userId);
}
