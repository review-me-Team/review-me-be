package reviewme.be.resume.entity;

import lombok.*;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scope_id")
    private Scope scope;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    private String title;
    private String url;
    private int year;
    private Integer commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public static Resume ofCreated(UploadResumeRequest uploadResumeRequest, User user, Scope scope, Occupation occupation, String fileName) {
        return Resume.builder()
                .user(user)
                .scope(scope)
                .occupation(occupation)
                .title(uploadResumeRequest.getTitle())
                .url(fileName)
                .year(uploadResumeRequest.getYear())
                .commentCnt(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void softDelete() {

        this.deletedAt = LocalDateTime.now();
    }

    public void update(UpdateResumeRequest updateResumeRequest, Scope scope, Occupation occupation) {

        this.title = updateResumeRequest.getTitle();
        this.scope = scope;
        this.occupation = occupation;
        this.year = updateResumeRequest.getYear();
    }
}
