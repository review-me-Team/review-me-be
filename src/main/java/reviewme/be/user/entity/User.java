package reviewme.be.user.entity;

import lombok.*;
import reviewme.be.user.dto.UserGitHubProfile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long githubId;
    private String name;
    private String profileUrl;

    public User(UserGitHubProfile userGitHubProfile) {

        this.githubId = userGitHubProfile.getId();
        this.name = userGitHubProfile.getLogin();
        this.profileUrl = userGitHubProfile.getAvatarUrl();
    }
}
