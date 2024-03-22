package reviewme.be.feedback.entity;

import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import reviewme.be.util.entity.Emoji;
import reviewme.be.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_id")
    private Emoji emoji;

    public FeedbackEmoji(User user, Feedback feedback, Emoji emoji) {
        this.user = user;
        this.feedback = feedback;
        this.emoji = emoji;
    }

    public void updateEmoji(Emoji emoji) {

        this.emoji = emoji;
    }

    public static List<FeedbackEmoji> createDefaultFeedbackEmojis(Feedback feedback,
        List<Emoji> emojis) {

        return emojis.stream()
            .map(emoji -> FeedbackEmoji.builder()
                .feedback(feedback)
                .emoji(emoji)
                .build())
            .collect(Collectors.toList());
    }
}
