package reviewme.be.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.comment.dto.response.CommentResponse;

public interface CommentRepositoryCustom {

    Page<CommentResponse> findCommentsByResumeId(long resumeId, long userId, Pageable pageable);
}
