package reviewme.be.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByResumeIdOrderByCreatedAtDesc(long resumeId);


}
