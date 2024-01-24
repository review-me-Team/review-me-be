package reviewme.be.comment.entity;

import lombok.*;
import reviewme.be.resume.entity.Resume;
import reviewme.be.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static Comment ofCreated(User commenter, Resume resume, String content) {

        return Comment.builder()
                .commenter(commenter)
                .resume(resume)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void softDelete(LocalDateTime deletedAt) {

        this.deletedAt = deletedAt;
    }

    public void updateContent(String content, LocalDateTime updatedAt) {

        this.content = content;
        this.updatedAt = updatedAt;
    }
}
