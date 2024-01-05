package reviewme.be.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reviewme.be.login.response.UserProfileResponse;
import reviewme.be.login.token.GitHubOAuthApp;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final RestTemplate restTemplate;
    private final GitHubOAuthApp gitHubOAuthApp;

    public UserProfileResponse getUserProfile(String code) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.setAll(header);

        MultiValueMap<String, String> payloads = new LinkedMultiValueMap<>();
        Map<String, String> payload = new HashMap<>();
        payload.put("grant_type", "authorization_code");
        payload.put("client_id", gitHubOAuthApp.getClientId());
        payload.put("client_secret", gitHubOAuthApp.getClientSecret());
        payload.put("code", code);
        payloads.setAll(payload);

        HttpEntity<?> request = new HttpEntity<>(payloads, headers);

        return UserProfileResponse.builder()
                .id(1L)
                .name("aken-you")
                .avatarUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                .build();
    }
}
