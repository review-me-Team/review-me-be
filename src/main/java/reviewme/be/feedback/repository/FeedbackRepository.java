package reviewme.be.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.feedback.entity.Feedback;


import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByResumeIdAndResumePage(long resumeId, int resumePage);
}
