package reviewme.be.comment.repository;

import static com.querydsl.core.types.ExpressionUtils.count;
import static reviewme.be.comment.entity.QCommentEmoji.commentEmoji;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reviewme.be.util.dto.EmojiCount;

@RequiredArgsConstructor
public class CommentEmojiRepositoryImpl implements CommentEmojiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmojiCount> findCommentEmojiCountByCommentId(long commentId) {

        return queryFactory
            .select(Projections.constructor(EmojiCount.class,
                commentEmoji.emoji.id,
                count(commentEmoji.emoji.id)))
            .from(commentEmoji)
            .innerJoin(commentEmoji.emoji)
            .innerJoin(commentEmoji.user)
            .where(commentEmoji.comment.id.eq(commentId))
            .groupBy(commentEmoji.emoji.id)
            .orderBy(commentEmoji.emoji.id.asc())
            .fetch();
    }
}
