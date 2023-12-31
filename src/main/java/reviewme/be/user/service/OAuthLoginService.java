package reviewme.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reviewme.be.user.dto.UserGitHubAccessToken;
import reviewme.be.user.dto.UserGitHubProfile;
import reviewme.be.user.dto.response.UserProfileResponse;
import reviewme.be.user.exception.InvalidCodeException;
import reviewme.be.user.token.GitHubOAuthApp;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final RestTemplate restTemplate;
    private final GitHubOAuthApp gitHubOAuthApp;

    public UserProfileResponse getUserProfile(String code) {

        String accessToken = getAccessToken(code);
        UserGitHubProfile userGitHubProfile = getUserGitHubProfile(accessToken);

        return UserProfileResponse.builder()
                .id(1L)
                .name(userGitHubProfile.getLogin())
                .avatarUrl(userGitHubProfile.getAvatarUrl())
                .build();
    }

    /**
     * Call the GitHub API with the code to get the accessToken.
     *
     * @param code
     * @return accessToken
     */
    private String getAccessToken(String code) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.setAll(header);

        MultiValueMap<String, String> payloads = new LinkedMultiValueMap<>();
        Map<String, String> payload = new HashMap<>();
        payload.put("client_id", gitHubOAuthApp.getClientId());
        payload.put("client_secret", gitHubOAuthApp.getClientSecret());
        payload.put("code", code);
        payloads.setAll(payload);

        HttpEntity<?> request = new HttpEntity<>(payloads, headers);
        ResponseEntity<UserGitHubAccessToken> response = restTemplate.postForEntity(
                gitHubOAuthApp.getAccessTokenEndpoint(),
                request,
                UserGitHubAccessToken.class);

        String accessToken = response.getBody().getAccessToken();

        if (accessToken == null) {
            throw new InvalidCodeException("[ERROR] 유효하지 않은 코드입니다.");
        }

        return response.getBody().getAccessToken();
    }

    /**
     * Call the GitHub API with the accessToken to get the user information.
     *
     * @param accessToken
     * @return UserGitHubProfile
     */
    private UserGitHubProfile getUserGitHubProfile(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                gitHubOAuthApp.getUserProfileEndpoint(),
                HttpMethod.GET,
                request,
                UserGitHubProfile.class).getBody();
    }
}
