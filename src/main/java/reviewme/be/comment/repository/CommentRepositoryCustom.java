package reviewme.be.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.comment.dto.CommentInfo;

public interface CommentRepositoryCustom {

    Page<CommentInfo> findCommentsByResumeId(long resumeId, Pageable pageable);

}
