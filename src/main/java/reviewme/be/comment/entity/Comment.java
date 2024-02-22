package reviewme.be.comment.entity;

import lombok.*;
import reviewme.be.resume.entity.Resume;
import reviewme.be.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Comment(User commenter, Resume resume, String content, LocalDateTime createdAt) {

        this.commenter = commenter;
        this.resume = resume;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void validateUser(User user) {

        this.commenter.validateSameUser(user);
    }

    public void softDelete(LocalDateTime deletedAt) {

        this.deletedAt = deletedAt;
    }

    public void updateContent(String content, LocalDateTime updatedAt) {

        this.content = content;
        this.updatedAt = updatedAt;
    }
}
