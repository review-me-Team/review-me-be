package reviewme.be.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reviewme.be.login.response.UserProfileResponse;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final RestTemplate restTemplate;

    public UserProfileResponse getUserProfile(String code) {


        return UserProfileResponse.builder()
                .id(1L)
                .name("aken-you")
                .avatarUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                .build();
    }
}
