package reviewme.be.feedback.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.feedback.dto.FeedbackCommentInfo;
import reviewme.be.feedback.dto.FeedbackInfo;
import reviewme.be.feedback.dto.response.FeedbackResponse;
import reviewme.be.feedback.entity.Feedback;
import reviewme.be.util.dto.EmojiCount;

public interface FeedbackRepositoryCustom {

    Page<FeedbackResponse> findFeedbacksByResumeIdAndResumePage(long resumeId, long userId, int resumePage, Pageable pageable);

    List<EmojiCount> findFeedbackEmojiCountByFeedbackId(long feedbackId);

    Page<FeedbackCommentInfo> findFeedbackCommentsByParentId(long feedbackId, long userId, Pageable pageable);

    Optional<Feedback> findParentFeedbackByIdAndResumeId(long feedbackId, long resumeId);
}
