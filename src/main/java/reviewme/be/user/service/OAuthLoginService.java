package reviewme.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reviewme.be.user.dto.UserGitHubToken;
import reviewme.be.user.dto.UserGitHubProfile;
import reviewme.be.user.dto.UserRefreshedToken;
import reviewme.be.user.exception.InvalidCodeException;
import reviewme.be.user.token.GitHubOAuthApp;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final RestTemplate restTemplate;
    private final GitHubOAuthApp gitHubOAuthApp;

    public UserGitHubToken getUserGitHubToken(String code) {

        return getUserGitHubTokenByCode(code);
    }

    public UserRefreshedToken getUserRefreshedToken(String refreshToken) {

        return getUserRefreshedTokenFromGithub(refreshToken);
    }

    /**
     * Call the GitHub API with the code to get the accessToken.
     *
     * @param code
     * @return accessToken
     */
    private UserGitHubToken getUserGitHubTokenByCode(String code) {

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
        ResponseEntity<UserGitHubToken> response = restTemplate.postForEntity(
                gitHubOAuthApp.getAccessTokenEndpoint(),
                request,
                UserGitHubToken.class);

        String accessToken = response.getBody().getAccessToken();

        if (accessToken == null) {
            throw new InvalidCodeException("유효하지 않은 authorization code입니다.");
        }

        return response.getBody();
    }

    /**
     * Call the GitHub API with the accessToken to get the user information.
     *
     * @param accessToken
     * @return UserGitHubProfile
     */
    public UserGitHubProfile getUserGitHubProfile(String accessToken) {

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

    private UserRefreshedToken getUserRefreshedTokenFromGithub(String refreshToken) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.setAll(header);

        MultiValueMap<String, String> payloads = new LinkedMultiValueMap<>();
        Map<String, String> payload = new HashMap<>();
        payload.put("client_id", gitHubOAuthApp.getClientId());
        payload.put("client_secret", gitHubOAuthApp.getClientSecret());
        payload.put("grant_type", gitHubOAuthApp.getGrantType());
        payload.put("refresh_token", refreshToken);
        payloads.setAll(payload);

        HttpEntity<?> request = new HttpEntity<>(payloads, headers);
        ResponseEntity<UserRefreshedToken> response = restTemplate.postForEntity(
                gitHubOAuthApp.getAccessTokenEndpoint(),
                request,
                UserRefreshedToken.class);

        return response.getBody();
    }
}
