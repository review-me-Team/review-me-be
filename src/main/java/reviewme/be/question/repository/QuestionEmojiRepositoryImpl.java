package reviewme.be.question.repository;

import static com.querydsl.core.types.ExpressionUtils.count;
import static reviewme.be.question.entity.QQuestion.question;
import static reviewme.be.question.entity.QQuestionEmoji.questionEmoji;
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
public class QuestionEmojiRepositoryImpl implements QuestionEmojiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmojiCount> findEmojiCountByQuestionIds(List<Long> questionIds) {

        return queryFactory
            .select(new QEmojiCount(
                emoji.id,
                count(user.id)
            ))
            .from(questionEmoji)
            .rightJoin(questionEmoji.emoji, emoji)
            .leftJoin(questionEmoji.user, user)
            .innerJoin(questionEmoji.question, question)
            .where(questionEmoji.question.id.in(questionIds))
            .groupBy(emoji.id, question.id)
            .orderBy(question.id.desc(), emoji.id.asc())
            .fetch();
    }
}
