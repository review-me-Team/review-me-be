package reviewme.be.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reviewme.be.question.entity.QuestionEmoji;

import javax.persistence.Tuple;
import java.util.List;

public interface QuestionEmojiRepository extends JpaRepository<QuestionEmoji, Long>, QuestionEmojiRepositoryCustom {

    List<QuestionEmoji> findByQuestionId(long questionId);

    @Query(value = "SELECT emoji.id AS id, COUNT(emoji.id) AS count " +
            "FROM QuestionEmoji questionEmoji " +
            "JOIN questionEmoji.emoji emoji " +
            "WHERE questionEmoji.question.id = :questionId " +
            "GROUP BY emoji.id")
    List<Tuple> countByQuestionIdGroupByEmojiId(long questionId);

    QuestionEmoji findByQuestionIdAndUserId(long questionId, long userId);
}
