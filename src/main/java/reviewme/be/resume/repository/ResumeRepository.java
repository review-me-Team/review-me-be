package reviewme.be.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.resume.entity.Resume;


import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserId(long userId);
}
