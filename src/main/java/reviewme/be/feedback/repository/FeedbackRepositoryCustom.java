package reviewme.be.feedback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.feedback.dto.FeedbackCommentInfo;
import reviewme.be.feedback.dto.FeedbackInfo;

public interface FeedbackRepositoryCustom {

    Page<FeedbackInfo> findFeedbacksByResumeIdAndResumePage(long resumeId, long userId, int resumePage, Pageable pageable);

    Page<FeedbackCommentInfo> findFeedbackCommentsByFeedbackId(long feedbackId, long userId, Pageable pageable);
}
