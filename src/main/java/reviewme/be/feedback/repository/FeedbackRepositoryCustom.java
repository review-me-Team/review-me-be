package reviewme.be.feedback.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.feedback.dto.FeedbacksFilter;
import reviewme.be.feedback.dto.response.FeedbackCommentResponse;
import reviewme.be.feedback.dto.response.FeedbackResponse;
import reviewme.be.feedback.entity.Feedback;

public interface FeedbackRepositoryCustom {

    Page<FeedbackResponse> findFeedbacksByResumeIdAndResumePage(long resumeId, long userId, FeedbacksFilter filter, Pageable pageable);

    Page<FeedbackCommentResponse> findFeedbackCommentsByParentId(long feedbackId, long userId, Pageable pageable);

    Optional<Feedback> findParentFeedbackByIdAndResumeId(long feedbackId, long resumeId);
}
