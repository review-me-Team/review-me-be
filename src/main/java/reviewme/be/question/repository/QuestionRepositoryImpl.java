package reviewme.be.question.repository;

import static reviewme.be.question.entity.QQuestion.question;
import static reviewme.be.question.entity.QQuestionEmoji.questionEmoji;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.question.dto.QuestionsFilter;
import reviewme.be.question.dto.response.QQuestionCommentResponse;
import reviewme.be.question.dto.response.QQuestionResponse;
import reviewme.be.question.dto.response.QuestionCommentResponse;
import reviewme.be.question.dto.response.QuestionResponse;
import reviewme.be.question.entity.Question;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<QuestionResponse> findQuestionsByResumeIdAndResumePage(long resumeId, long userId,
        QuestionsFilter filter,
        Pageable pageable) {

        QueryResults<QuestionResponse> results = queryFactory
            .select(new QQuestionResponse(
                question.id,
                question.content,
                question.commenter.id,
                question.commenter.name,
                question.commenter.profileUrl,
                question.labelContent,
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
            .where(question.resume.id.eq(resumeId)
                .and(question.resumePage.eq(filter.getResumePage()))
                .and(checked(filter.getChecked()))
                .and(bookmarked(filter.getBookmarked()))
                .and(question.deletedAt.isNull()
                    .or(question.deletedAt.isNotNull().and(question.childCnt.gt(0))))
            )
            .orderBy(question.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<QuestionResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionCommentResponse> findQuestionCommentsByQuestionId(long questionId,
        long userId, Pageable pageable) {

        QueryResults<QuestionCommentResponse> results = queryFactory
            .select(new QQuestionCommentResponse(
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
            .orderBy(question.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<QuestionCommentResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Question> findParentQuestionByIdAndResumeId(long questionId, long resumeId) {

        return Optional.ofNullable(queryFactory
            .selectFrom(question)
            .where(question.id.eq(questionId)
                .and(question.resume.id.eq(resumeId))
                .and(question.parentQuestion.isNull())
                .and(question.deletedAt.isNull()
                    .or(question.deletedAt.isNotNull().and(question.childCnt.gt(0))))
            )
            .fetchOne());
    }

    private BooleanExpression checked(Boolean checked) {

        return checked != null ? question.checked.eq(checked) : null;
    }

    private BooleanExpression bookmarked(Boolean bookmarked) {

        return bookmarked != null ? question.bookmarked.eq(bookmarked) : null;
    }
}
