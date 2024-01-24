package reviewme.be.comment.entity;

import lombok.*;
import reviewme.be.util.entity.Emoji;
import reviewme.be.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToOne
    @JoinColumn(name = "emoji_id")
    private Emoji emoji;

    public static CommentEmoji ofCreated(User user, Comment comment, Emoji emoji) {
        return CommentEmoji.builder()
                .user(user)
                .comment(comment)
                .emoji(emoji)
                .build();
    }

    public void updateEmoji(Emoji emoji) {

        this.emoji = emoji;
    }
}
