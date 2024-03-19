package reviewme.be.comment.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import reviewme.be.resume.entity.Resume;
import reviewme.be.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
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

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public Comment(User commenter, Resume resume, String content) {

        this.commenter = commenter;
        this.resume = resume;
        this.content = content;
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
