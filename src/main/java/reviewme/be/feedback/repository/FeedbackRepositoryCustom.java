package reviewme.be.feedback.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.feedback.dto.FeedbackCommentInfo;
import reviewme.be.feedback.dto.FeedbackInfo;
import reviewme.be.feedback.entity.Feedback;

public interface FeedbackRepositoryCustom {

    Page<FeedbackInfo> findFeedbacksByResumeIdAndResumePage(long resumeId, long userId, int resumePage, Pageable pageable);

    Page<FeedbackCommentInfo> findFeedbackCommentsByParentId(long feedbackId, long userId, Pageable pageable);

    Optional<Feedback> findParentFeedbackByIdAndResumeId(long feedbackId, long resumeId);
}
