package reviewme.be.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.feedback.entity.FeedbackEmoji;

import java.util.List;

public interface FeedbackEmojiRepository extends JpaRepository<FeedbackEmoji, Long> {

    List<FeedbackEmoji> findByFeedbackId(long feedbackId);
}
