package reviewme.be.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.question.entity.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByResumeIdAndResumePage(long resumeId, int resumePage);
}
