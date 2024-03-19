package reviewme.be.resume.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import reviewme.be.resume.dto.request.UpdateResumeRequest;
import reviewme.be.resume.dto.request.UploadResumeRequest;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;
import reviewme.be.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scope_id")
    private Scope scope;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    private String title;
    private String url;
    private int year;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public static Resume ofCreated(UploadResumeRequest uploadResumeRequest, User user, Scope scope,
        Occupation occupation, String fileName) {
        return Resume.builder()
            .writer(user)
            .scope(scope)
            .occupation(occupation)
            .title(uploadResumeRequest.getTitle())
            .url(fileName)
            .year(uploadResumeRequest.getYear())
            .build();
    }

    public void validateUser(User user) {

        this.writer.validateSameUser(user);
    }

    public void softDelete(LocalDateTime deletedAt) {

        this.deletedAt = deletedAt;
    }

    public void update(UpdateResumeRequest updateResumeRequest, Scope scope,
        Occupation occupation) {

        this.title = updateResumeRequest.getTitle();
        this.scope = scope;
        this.occupation = occupation;
        this.year = updateResumeRequest.getYear();
    }

    public boolean isPublic() {

        return this.scope.getId() == 1;
    }

    public boolean isFriendsOnly() {

        return this.scope.getId() == 2;
    }

    public boolean isPrivate() {

        return this.scope.getId() == 3;
    }

    public boolean isWriter(User user) {

        return this.writer.equals(user);
    }
}
