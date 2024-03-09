package reviewme.be.question.repository;

import static reviewme.be.question.entity.QQuestion.question;
import static reviewme.be.question.entity.QQuestionEmoji.questionEmoji;
import static reviewme.be.util.entity.QLabel.label;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.question.dto.QQuestionCommentInfo;
import reviewme.be.question.dto.QQuestionInfo;
import reviewme.be.question.dto.QuestionCommentInfo;
import reviewme.be.question.dto.QuestionInfo;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<QuestionInfo> findQuestionsByResumeIdAndResumePage(long resumeId, long userId,
        int resumePage,
        Pageable pageable) {

        QueryResults<QuestionInfo> results = queryFactory
            .select(new QQuestionInfo(
                question.id,
                question.content,
                label.content,
                question.commenter.id,
                question.commenter.name,
                question.commenter.profileUrl,
                question.createdAt,
                question.childCnt,
                question.checked,
                question.bookmarked,
                JPAExpressions
                    .select(questionEmoji.emoji.id)
                    .from(questionEmoji)
                    .where(questionEmoji.question.id.eq(question.id)
                        .and(questionEmoji.user.id.eq(userId))
                    )
            ))
            .from(question)
            .innerJoin(question.commenter)
            .leftJoin(question.label, label)
            .where(question.resume.id.eq(resumeId)
                .and(question.resumePage.eq(resumePage))
                .and(question.deletedAt.isNull()
                    .or(question.deletedAt.isNotNull().and(question.childCnt.gt(0))))
            )
            .orderBy(question.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<QuestionInfo> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionCommentInfo> findQuestionCommentsByQuestionId(long questionId,
        long userId, Pageable pageable) {

        QueryResults<QuestionCommentInfo> results = queryFactory
            .select(new QQuestionCommentInfo(
                question.id,
                question.parentQuestion.id,
                question.content,
                question.commenter.id,
                question.commenter.name,
                question.commenter.profileUrl,
                question.createdAt,
                JPAExpressions
                    .select(questionEmoji.emoji.id)
                    .from(questionEmoji)
                    .where(questionEmoji.question.id.eq(question.id)
                        .and(questionEmoji.user.id.eq(userId))
                    )
            ))
            .from(question)
            .innerJoin(question.commenter)
            .where(question.parentQuestion.id.eq(questionId)
                .and(question.deletedAt.isNull())
            )
            .orderBy(question.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<QuestionCommentInfo> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
