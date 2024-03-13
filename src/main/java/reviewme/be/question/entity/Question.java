package reviewme.be.question.entity;

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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User commenter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Question parentQuestion;

    private String labelContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String content;
    private Integer resumePage;
    private Boolean bookmarked;
    private Boolean checked;
    private Long childCnt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public static Question createQuestion(User commenter, Resume resume, String labelContent, String content, Integer resumePage) {

        return Question.builder()
            .commenter(commenter)
            .resume(resume)
            .labelContent(labelContent)
            .content(content)
            .resumePage(resumePage)
            .bookmarked(false)
            .checked(false)
            .childCnt(0L)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public static Question createQuestionComment(User commenter, Resume resume, Question parentQuestion, String content) {

        return Question.builder()
            .commenter(commenter)
            .resume(resume)
            .parentQuestion(parentQuestion)
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

    public void updateContent(String labelContent, String content) {

        this.labelContent = labelContent;
        this.content = content;
    }

    public void updateChecked(boolean checked) {

        this.checked = checked;
    }

    public void updateBookmarked(boolean bookmarked) {

        this.bookmarked = bookmarked;
    }

    public void softDelete(LocalDateTime deletedAt) {

        this.deletedAt = deletedAt;
    }

    public boolean isParentQuestion() {

        return this.parentQuestion == null;
    }
}
