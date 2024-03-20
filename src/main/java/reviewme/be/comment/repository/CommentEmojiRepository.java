package reviewme.be.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reviewme.be.comment.entity.CommentEmoji;

import java.util.List;
import java.util.Optional;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long>, CommentEmojiRepositoryCustom {

    Optional<CommentEmoji> findByUserIdAndCommentId(long userId, long commentId);

    @Modifying
    @Query(value = "DELETE FROM comment_emoji WHERE comment_id = :commentId", nativeQuery = true)
    void deleteAllByCommentId(@Param("commentId") long commentId);
}
