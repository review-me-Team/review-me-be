package reviewme.be.question.entity;

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
public class QuestionEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_id")
    private Emoji emoji;

    public static List<QuestionEmoji> createDefaultQuestionEmojis(Question question,
        List<Emoji> emojis) {

        return emojis.stream()
            .map(emoji -> QuestionEmoji.builder()
                .question(question)
                .emoji(emoji)
                .build())
            .collect(Collectors.toList());
    }
}
