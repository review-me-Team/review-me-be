package reviewme.be.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reviewme.be.question.entity.QuestionEmoji;

import javax.persistence.Tuple;
import java.util.List;

public interface QuestionEmojiRepository extends JpaRepository<QuestionEmoji, Long> {

    List<QuestionEmoji> findByQuestionId(long questionId);

    QuestionEmoji findByQuestionIdAndUserId(long questionId, long userId);
}
