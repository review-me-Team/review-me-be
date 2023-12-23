package reviewme.be.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reviewme.be.feedback.entity.FeedbackEmoji;

import javax.persistence.Tuple;
import java.util.List;

public interface FeedbackEmojiRepository extends JpaRepository<FeedbackEmoji, Long> {

    List<FeedbackEmoji> findByFeedbackId(long feedbackId);

    @Query(value = "SELECT emoji.id AS id, COUNT(emoji.id) AS count " +
            "FROM FeedbackEmoji feedbackEmoji " +
            "JOIN feedbackEmoji.emoji emoji " +
            "WHERE feedbackEmoji.feedback.id = :feedbackId " +
            "GROUP BY emoji.id")
    List<Tuple> countByFeedbackIdGroupByEmojiId(long feedbackId);

    FeedbackEmoji findByFeedbackIdAndUserId(long feedbackId, long userId);
}
