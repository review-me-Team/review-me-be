package reviewme.be.comment.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import reviewme.be.comment.dto.response.CommentResponse;
import reviewme.be.comment.dto.response.QCommentResponse;

import static reviewme.be.comment.entity.QComment.comment;
import static reviewme.be.comment.entity.QCommentEmoji.commentEmoji;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentResponse> findCommentsByResumeId(long resumeId, long userId, Pageable pageable) {

        QueryResults<CommentResponse> results = queryFactory
            .select(new QCommentResponse(
                comment.id,
                comment.content,
                comment.commenter.id,
                comment.commenter.name,
                comment.commenter.profileUrl,
                comment.createdAt,
                JPAExpressions
                    .select(commentEmoji.emoji.id)
                    .from(commentEmoji)
                    .where(commentEmoji.comment.id.eq(comment.id)
                        .and(commentEmoji.user.id.eq(userId))
                    )
            ))
            .from(comment)
            .leftJoin(comment.commenter)
            .where(comment.resume.id.eq(resumeId)
                .and(comment.deletedAt.isNull())
            )
            .orderBy(comment.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<CommentResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
