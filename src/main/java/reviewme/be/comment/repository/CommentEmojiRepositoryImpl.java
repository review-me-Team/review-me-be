package reviewme.be.comment.repository;

import static com.querydsl.core.types.ExpressionUtils.count;
import static reviewme.be.comment.entity.QComment.comment;
import static reviewme.be.comment.entity.QCommentEmoji.commentEmoji;
import static reviewme.be.user.entity.QUser.user;
import static reviewme.be.util.entity.QEmoji.emoji;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.dto.QEmojiCount;

@RequiredArgsConstructor
public class CommentEmojiRepositoryImpl implements CommentEmojiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmojiCount> findEmojiCountByCommentIds(List<Long> commentIds) {

        return queryFactory
            .select(new QEmojiCount(
                emoji.id,
                count(user.id)
            ))
            .from(commentEmoji)
            .rightJoin(commentEmoji.emoji, emoji)
            .leftJoin(commentEmoji.user, user)
            .innerJoin(commentEmoji.comment, comment)
            .where(commentEmoji.comment.id.in(commentIds))
            .groupBy(emoji.id, comment.id)
            .orderBy(comment.id.desc(), emoji.id.asc())
            .fetch();
    }
}
