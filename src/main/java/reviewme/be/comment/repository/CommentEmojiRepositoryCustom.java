package reviewme.be.comment.repository;

import java.util.List;
import reviewme.be.comment.entity.CommentEmoji;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.dto.MyEmoji;

public interface CommentEmojiRepositoryCustom {

    List<EmojiCount> findEmojiCountByCommentIds(List<Long> commentIds);

    List<MyEmoji> findByUserIdAndCommentIdIn(long userId, List<Long> commentIds);
}
