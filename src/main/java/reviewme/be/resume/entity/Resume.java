package reviewme.be.resume.entity;

import lombok.*;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;
import reviewme.be.util.entity.User;

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
    private Integer year;
    private Integer commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
