package reviewme.be.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.question.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByResumeIdAndResumePage(long resumeId, int resumePage);

    Optional<Question> findByIdAndDeletedAtIsNull(long questionId);

    Optional<Question> findByIdAndResumeIdAndDeletedAtIsNull(long questionId, long resumeId);

    Optional<Question> findByIdAndResumeIdAndResumePageAndDeletedAtIsNull(long questionId, long resumeId, int resumePage);
}
