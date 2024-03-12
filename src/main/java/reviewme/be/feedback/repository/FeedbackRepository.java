package reviewme.be.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reviewme.be.feedback.entity.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {

    List<Feedback> findByResumeIdAndResumePage(long resumeId, int resumePage);

    Optional<Feedback> findByIdAndDeletedAtIsNull(long feedbackId);

    Optional<Feedback> findByIdAndResumeIdAndDeletedAtIsNull(long feedbackId, long resumeId);

    @Query("select f "
        + "from Feedback f "
        + "where f.id = :feedbackId "
        + "and (f.deletedAt is null or (f.deletedAt is not null and f.childCnt > 0)) ")
    Optional<Feedback> findParentFeedbackById(@Param("feedbackId") long feedbackId);
}
