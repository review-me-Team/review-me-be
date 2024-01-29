package reviewme.be.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.user.entity.User;

@Getter
@Builder
@Schema(description = "로그인 유저 정보 응답")
public class UserProfileResponse {

    @Schema(description = "유저 ID", example = "1")
    private long id;

    @Schema(description = "유저 이름", example = "aken-you")
    private String name;

    @Schema(description = "유저 GitHub 이미지 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    @JsonProperty("avatar_url")
    private String avatarUrl;

    public static UserProfileResponse fromUser(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .avatarUrl(user.getProfileUrl())
                .build();
    }
}