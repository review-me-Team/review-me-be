package reviewme.be.question.repository;

import static com.querydsl.core.types.ExpressionUtils.count;
import static reviewme.be.question.entity.QQuestionEmoji.questionEmoji;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reviewme.be.util.dto.EmojiCount;

@RequiredArgsConstructor
public class QuestionEmojiRepositoryImpl implements QuestionEmojiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmojiCount> findQuestionEmojiCountByQuestionId(long questionId) {

        return queryFactory
            .select(Projections.constructor(EmojiCount.class,
                questionEmoji.emoji.id,
                count(questionEmoji.emoji.id)))
            .from(questionEmoji)
            .innerJoin(questionEmoji.emoji)
            .innerJoin(questionEmoji.user)
            .where(questionEmoji.question.id.eq(questionId))
            .groupBy(questionEmoji.emoji.id)
            .orderBy(questionEmoji.emoji.id.asc())
            .fetch();
    }
}
