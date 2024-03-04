package reviewme.be.feedback.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.feedback.entity.FeedbackEmoji;

public interface FeedbackEmojiRepository extends JpaRepository<FeedbackEmoji, Long>, FeedbackEmojiRepositoryCustom {

    Optional<FeedbackEmoji> findByFeedbackIdAndUserId(long feedbackId, long userId);

    Optional<FeedbackEmoji> findByUserIdAndFeedbackId(long userId, long feedbackId);

    void deleteAllByFeedbackId(long feedbackId);
}
