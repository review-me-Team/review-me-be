package reviewme.be.comment.entity;

import lombok.*;
import reviewme.be.util.entity.Emoji;
import reviewme.be.util.entity.User;

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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_id")
    private Emoji emoji;
}
