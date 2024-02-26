package reviewme.be.feedback.repository;

import static com.querydsl.core.types.ExpressionUtils.count;
import static reviewme.be.feedback.entity.QFeedback.feedback;
import static reviewme.be.feedback.entity.QFeedbackEmoji.feedbackEmoji;
import static reviewme.be.user.entity.QUser.user;
import static reviewme.be.util.entity.QEmoji.emoji;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.dto.MyEmoji;
import reviewme.be.util.dto.QEmojiCount;
import reviewme.be.util.dto.QMyEmoji;

@RequiredArgsConstructor
public class FeedbackEmojiRepositoryImpl implements FeedbackEmojiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmojiCount> findEmojiCountByFeedbackIds(List<Long> feedbackIds) {

        return queryFactory
            .select(new QEmojiCount(
                emoji.id,
                count(user.id)
            ))
            .from(feedbackEmoji)
            .rightJoin(feedbackEmoji.emoji, emoji)
            .leftJoin(feedbackEmoji.user, user)
            .innerJoin(feedbackEmoji.feedback, feedback)
            .where(feedbackEmoji.feedback.id.in(feedbackIds))
            .groupBy(emoji.id, feedback.id)
            .orderBy(feedback.createdAt.desc(), emoji.id.asc())
            .fetch();
    }

    @Override
    public List<MyEmoji> findMyEmojiIdsByFeedbackIdIn(long userId, List<Long> feedbackIds) {

        return queryFactory
            .select(new QMyEmoji(
                feedbackEmoji.emoji.id,
                user.id)
            )
            .from(feedbackEmoji)
            .leftJoin(feedbackEmoji.user, user)
            .where(feedbackEmoji.feedback.id.in(feedbackIds)
                .and(feedbackEmoji.user.id.eq(userId))
                .or(user.id.isNull()))
            .groupBy(feedbackEmoji.feedback.id)
            .orderBy(feedbackEmoji.feedback.id.desc())
            .fetch();
    }
}
