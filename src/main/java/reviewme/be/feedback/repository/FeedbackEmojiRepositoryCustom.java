package reviewme.be.feedback.repository;

import java.util.List;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.dto.MyEmoji;

public interface FeedbackEmojiRepositoryCustom {

    List<EmojiCount> findEmojiCountByFeedbackIds(List<Long> feedbackIds);

    List<MyEmoji> findMyEmojiIdsByFeedbackIdIn(long userId, List<Long> feedbackIds);
}
