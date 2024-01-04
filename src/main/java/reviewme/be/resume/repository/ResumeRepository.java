package reviewme.be.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.resume.entity.Resume;


import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByUrl(String url);
    List<Resume> findByUserId(long userId);
}
