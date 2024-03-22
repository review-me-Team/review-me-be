package reviewme.be.question.repository;

import java.util.List;
import reviewme.be.util.dto.EmojiCount;

public interface QuestionEmojiRepositoryCustom {

    List<EmojiCount> findQuestionEmojiCountByQuestionId(long questionId);
}
