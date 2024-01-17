package reviewme.be.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.user.entity.User;

@Getter
@Builder
@Schema(description = "사용자 정보")
public class UserResponse {

    @Schema(description = "사용자 ID", example = "2")
    private long id;

    @Schema(description = "사용자 이름", example = "acceptor-gyu")
    private String name;

    @Schema(description = "사용자 프로필 url", example = "https://avatars.githubusercontent.com/u/71162390?v=4")
    private String profileUrl;

    public static UserResponse fromUser(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .profileUrl(user.getProfileUrl())
                .build();
    }

    @QueryProjection
    public UserResponse(long id, String name, String profileUrl) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
    }
}
