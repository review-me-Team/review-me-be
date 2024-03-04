package reviewme.be.question.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.question.entity.QuestionEmoji;

public interface QuestionEmojiRepository extends JpaRepository<QuestionEmoji, Long>, QuestionEmojiRepositoryCustom {

    Optional<QuestionEmoji> findByQuestionIdAndUserId(long questionId, long userId);

    void deleteAllByQuestionId(long questionId);
}
