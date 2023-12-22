package reviewme.be.feedback.entity;

import lombok.*;
import reviewme.be.resume.entity.Resume;
import reviewme.be.util.entity.Label;
import reviewme.be.util.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Feedback parentFeedback;

    @OneToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @OneToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String content;
    private int resumePage;
    private Boolean checked;
    private int childCnt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
