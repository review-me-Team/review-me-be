package reviewme.be.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGitHubProfile {

    private String login;
    private String id;

    @JsonProperty("avatar_url")
    private String avatarUrl;
}
