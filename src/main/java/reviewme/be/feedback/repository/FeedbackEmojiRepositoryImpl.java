package reviewme.be.feedback.repository;

import static com.querydsl.core.types.ExpressionUtils.count;
import static reviewme.be.feedback.entity.QFeedback.feedback;
import static reviewme.be.feedback.entity.QFeedbackEmoji.feedbackEmoji;
import static reviewme.be.user.entity.QUser.user;
import static reviewme.be.util.entity.QEmoji.emoji;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.dto.QEmojiCount;

@RequiredArgsConstructor
public class FeedbackEmojiRepositoryImpl implements FeedbackEmojiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmojiCount> findFeedbackEmojiCountByFeedbackId(long feedbackId) {

        return queryFactory
            .select(Projections.constructor(EmojiCount.class,
                feedbackEmoji.emoji.id,
                count(feedbackEmoji.emoji.id)))
            .from(feedbackEmoji)
            .innerJoin(feedbackEmoji.emoji)
            .innerJoin(feedbackEmoji.user)
            .where(feedbackEmoji.feedback.id.eq(feedbackId))
            .groupBy(feedbackEmoji.emoji.id)
            .orderBy(feedbackEmoji.emoji.id.asc())
            .fetch();
    }
}
