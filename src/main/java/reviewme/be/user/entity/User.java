package reviewme.be.user.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import reviewme.be.user.dto.UserGitHubProfile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import reviewme.be.user.exception.NoAuthorizationException;

@Entity
@Getter
@Builder
@EqualsAndHashCode(of = "id")
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long githubId;
    private String name;
    private String profileUrl;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public User(UserGitHubProfile userGitHubProfile) {

        this.githubId = userGitHubProfile.getId();
        this.name = userGitHubProfile.getLogin();
        this.profileUrl = userGitHubProfile.getAvatarUrl();
    }

    public void validateSameUser(User user) {

        if (!this.equals(user)) {
            throw new NoAuthorizationException("권한이 없습니다.");
        }
    }

    public boolean isAnonymous() {

        long anonymousUserId = 0L;

        return this.id == anonymousUserId;
    }
}
