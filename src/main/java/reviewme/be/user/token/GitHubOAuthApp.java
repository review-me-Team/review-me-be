package reviewme.be.user.token;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Builder
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GitHubOAuthApp {


    @Value("${GITHUB_APP_CLIENT_ID}")
    private String clientId;

    @Value("${GITHUB_APP_SECRET}")
    private String clientSecret;

    @Value("${GITHUB_GRANT_TYPE}")
    private String grantType;

    @Value("${GITHUB_ACCESSTOKEN_ENDPOINT}")
    private String accessTokenEndpoint;

    @Value("${GITHUB_USER_PROFILE_ENDPOINT}")
    private String userProfileEndpoint;
}
