package reviewme.be.feedback.repository;

import java.util.List;
import reviewme.be.util.dto.EmojiCount;

public interface FeedbackEmojiRepositoryCustom {

    List<EmojiCount> findFeedbackEmojiCountByFeedbackId(long feedbackId);
}
