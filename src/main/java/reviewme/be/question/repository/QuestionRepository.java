package reviewme.be.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reviewme.be.question.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {

    Optional<Question> findByIdAndDeletedAtIsNull(long questionId);

    Optional<Question> findByIdAndResumeIdAndDeletedAtIsNull(long questionId, long resumeId);

    @Query("select q "
        + "from Question q "
        + "where q.id = :questionId "
        + "and (q.deletedAt is null or (q.deletedAt is not null and q.childCnt > 0)) ")
    Optional<Question> findQuestionById(@Param("questionId") long questionId);
}
