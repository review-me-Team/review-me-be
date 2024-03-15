package reviewme.be.feedback.repository;

import static reviewme.be.feedback.entity.QFeedback.feedback;
import static reviewme.be.feedback.entity.QFeedbackEmoji.feedbackEmoji;
import static reviewme.be.util.entity.QLabel.label;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.feedback.dto.FeedbackCommentInfo;
import reviewme.be.feedback.dto.FeedbackInfo;
import reviewme.be.feedback.dto.QFeedbackCommentInfo;
import reviewme.be.feedback.dto.QFeedbackInfo;
import reviewme.be.feedback.entity.Feedback;

@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FeedbackInfo> findFeedbacksByResumeIdAndResumePage(long resumeId, long userId,
        int resumePage,
        Pageable pageable) {

        QueryResults<FeedbackInfo> results = queryFactory
            .select(new QFeedbackInfo(
                feedback.id,
                feedback.content,
                label.content,
                feedback.commenter.id,
                feedback.commenter.name,
                feedback.commenter.profileUrl,
                feedback.createdAt,
                feedback.childCnt,
                feedback.checked,
                JPAExpressions
                    .select(feedbackEmoji.emoji.id)
                    .from(feedbackEmoji)
                    .where(feedbackEmoji.feedback.id.eq(feedback.id)
                        .and(feedbackEmoji.user.id.eq(userId))
                    )
            ))
            .from(feedback)
            .innerJoin(feedback.commenter)
            .leftJoin(feedback.label, label)
            .where(feedback.resume.id.eq(resumeId)
                .and(feedback.resumePage.eq(resumePage))
                .and(feedback.parentFeedback.isNull())
                .and(feedback.deletedAt.isNull()
                    .or(feedback.deletedAt.isNotNull().and(feedback.childCnt.gt(0))))
            )
            .orderBy(feedback.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<FeedbackInfo> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<FeedbackCommentInfo> findFeedbackCommentsByParentId(long feedbackId, long userId,
        Pageable pageable) {

        QueryResults<FeedbackCommentInfo> results = queryFactory
            .select(new QFeedbackCommentInfo(
                feedback.id,
                feedback.parentFeedback.id,
                feedback.content,
                feedback.commenter.id,
                feedback.commenter.name,
                feedback.commenter.profileUrl,
                feedback.createdAt,
                JPAExpressions
                    .select(feedbackEmoji.emoji.id)
                    .from(feedbackEmoji)
                    .where(feedbackEmoji.feedback.id.eq(feedback.id)
                        .and(feedbackEmoji.user.id.eq(userId))
                    )
            ))
            .from(feedback)
            .innerJoin(feedback.commenter)
            .where(feedback.parentFeedback.id.eq(feedbackId)
                .and(feedback.deletedAt.isNull())
            )
            .orderBy(feedback.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<FeedbackCommentInfo> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Feedback> findParentFeedbackByIdAndResumeId(long feedbackId, long resumeId) {

        return Optional.ofNullable(
            queryFactory
                .selectFrom(feedback)
                .where(feedback.id.eq(feedbackId)
                    .and(feedback.resume.id.eq(resumeId))
                    .and(feedback.parentFeedback.isNull())
                    .and(feedback.deletedAt.isNull()
                        .or(feedback.deletedAt.isNotNull().and(feedback.childCnt.gt(0))))
                )
                .fetchOne()
        );
    }
}
