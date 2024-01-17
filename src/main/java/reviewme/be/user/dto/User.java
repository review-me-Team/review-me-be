package reviewme.be.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사용자 정보")
public class User {

    @Schema(description = "사용자 ID", example = "2")
    private long id;

    @Schema(description = "사용자 이름", example = "acceptor-gyu")
    private String name;

    @Schema(description = "사용자 프로필 url", example = "https://avatars.githubusercontent.com/u/71162390?v=4")
    private String profileUrl;

    public static User fromUser(reviewme.be.util.entity.User user) {

        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .profileUrl(user.getProfileUrl())
                .build();
    }
}
