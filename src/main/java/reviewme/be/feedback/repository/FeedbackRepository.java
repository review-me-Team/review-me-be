package reviewme.be.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.feedback.entity.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {

    List<Feedback> findByResumeIdAndResumePage(long resumeId, int resumePage);

    Optional<Feedback> findByIdAndDeletedAtIsNull(long feedbackId);

    Optional<Feedback> findByIdAndResumeIdAndDeletedAtIsNull(long feedbackId, long resumeId);
}
