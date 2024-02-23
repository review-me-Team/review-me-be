package reviewme.be.feedback.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.feedback.dto.FeedbackInfo;
import reviewme.be.util.dto.EmojiCount;

public interface FeedbackRepositoryCustom {

    Page<FeedbackInfo> findFeedbacksByResumeIdAndResumePage(long resumeId, int resumePage, Pageable pageable);
}
