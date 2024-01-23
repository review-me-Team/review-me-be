package reviewme.be.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reviewme.be.comment.entity.CommentEmoji;

import javax.persistence.Tuple;
import java.util.List;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long>, CommentEmojiRepositoryCustom {

    CommentEmoji findByCommentIdAndUserId(long commentId, long userId);

    @Query(value = "SELECT commentEmoji " +
            "FROM CommentEmoji commentEmoji " +
            "WHERE commentEmoji.user.id = :userId " +
            "AND commentEmoji.user.id IN :commentIds")
    List<CommentEmoji> findByCommentIdAndUserIdIn(@Param("commentIds") List<Long> commentIds, @Param("userId")long userId);

    @Query(value = "SELECT emoji.id AS id, COUNT(emoji.id) AS count " +
            "FROM CommentEmoji commentEmoji " +
            "JOIN commentEmoji.emoji emoji " +
            "WHERE commentEmoji.comment.id = :commentId " +
            "GROUP BY emoji.id")
    List<Tuple> countByCommentIdGroupByEmojiId(@Param("commentId") long commentId);

    @Query(value = "SELECT comment.id AS commentId, emoji.id AS emojiId, COUNT(emoji.id) AS count " +
            "FROM CommentEmoji commentEmoji " +
            "JOIN commentEmoji.comment comment " +
            "JOIN commentEmoji.emoji emoji " +
            "WHERE comment.id IN :commentIds " +
            "GROUP BY comment.id, emoji.id")
    List<List<Tuple>> countByCommentIdGroupByEmojiIdIn(@Param("commentIds") List<Long> commentIds);

    @Modifying
    @Query(value = "DELETE FROM comment_emoji WHERE comment_id = :commentId", nativeQuery = true)
    void deleteAllByCommentId(@Param("commentId") long commentId);
}
