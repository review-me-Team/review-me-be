package reviewme.be.login.token;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Builder
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GitHubOAuthAppInfo {


    @Value("${GITHUB_CLIENT_ID_BE_LOCAL}")
    private String clientId;

    @Value("${GITHUB_CLIENT_SECRET_BE_LOCAL}")
    private String clientSecret;

    @Value("${GITHUB_REDIRECT_URI_BE_LOCAL}")
    private String redirectUri;
}
