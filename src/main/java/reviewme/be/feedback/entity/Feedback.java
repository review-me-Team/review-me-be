package reviewme.be.feedback.entity;

import lombok.*;
import reviewme.be.resume.entity.Resume;
import reviewme.be.util.entity.Label;
import reviewme.be.user.entity.User;

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
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User commenter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Feedback parentFeedback;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id")
    private Label label;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String content;
    private int resumePage;
    private boolean checked;
    private long childCnt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public static Feedback createdFeedback(User commenter, Resume resume, Label label, String content, int resumePage) {

        return Feedback.builder()
            .commenter(commenter)
            .resume(resume)
            .label(label)
            .content(content)
            .resumePage(resumePage)
            .checked(false)
            .childCnt(0)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public static Feedback createFeedbackComment(User commenter, Resume resume, Feedback parentFeedback, String content) {

        return Feedback.builder()
            .commenter(commenter)
            .resume(resume)
            .parentFeedback(parentFeedback)
            .content(content)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void validateUser(User user) {

        this.commenter.validateSameUser(user);
    }

    public void plusChildCnt() {

        this.childCnt++;
    }

    public void minusChildCnt() {

        this.childCnt--;
    }

    public void softDelete() {

        this.deletedAt = LocalDateTime.now();
    }

    public void updateContent(String content) {

        this.content = content;
    }

    public void updateChecked(boolean checked) {

        this.checked = checked;
    }

    public boolean isParentFeedback() {

        return this.parentFeedback == null;
    }
}
