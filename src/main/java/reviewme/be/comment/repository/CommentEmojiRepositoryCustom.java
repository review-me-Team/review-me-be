package reviewme.be.comment.repository;

import java.util.List;
import reviewme.be.util.dto.EmojiCount;

public interface CommentEmojiRepositoryCustom {

    List<EmojiCount> findEmojiCountByCommentIds(List<Long> commentIds);
}
