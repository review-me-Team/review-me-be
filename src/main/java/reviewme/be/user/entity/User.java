package reviewme.be.user.entity;

import java.time.LocalDateTime;
import lombok.*;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long githubId;
    private String name;
    private String profileUrl;
    private LocalDateTime createdAt;

    public User(UserGitHubProfile userGitHubProfile, LocalDateTime createdAt) {

        this.githubId = userGitHubProfile.getId();
        this.name = userGitHubProfile.getLogin();
        this.profileUrl = userGitHubProfile.getAvatarUrl();
        this.createdAt = createdAt;
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
