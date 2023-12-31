package reviewme.be.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reviewme.be.comment.entity.CommentEmoji;

import javax.persistence.Tuple;
import java.util.List;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long> {

    CommentEmoji findByCommentIdAndUserId(long commentId, long userId);

    @Query(value = "SELECT emoji.id AS id, COUNT(emoji.id) AS count " +
            "FROM CommentEmoji commentEmoji " +
            "JOIN commentEmoji.emoji emoji " +
            "WHERE commentEmoji.comment.id = :commentId " +
            "GROUP BY emoji.id")
    List<Tuple> countByCommentIdGroupByEmojiId(long commentId);
}
