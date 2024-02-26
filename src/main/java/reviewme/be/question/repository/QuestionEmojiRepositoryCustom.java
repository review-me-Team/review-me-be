package reviewme.be.question.repository;

import java.util.List;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.dto.MyEmoji;

public interface QuestionEmojiRepositoryCustom {

    List<EmojiCount> findEmojiCountByQuestionIds(List<Long> questionIds);

    List<MyEmoji> findMyEmojiIdsByQuestionIds(long userId, List<Long> questionIds);
}
