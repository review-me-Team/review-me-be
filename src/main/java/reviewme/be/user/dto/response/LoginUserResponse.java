package reviewme.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "로그인 유저에 대한 토큰")
public class LoginUserResponse {

    @Schema(description = "로그인 사용자 토큰", example = "abcdafeqwe.aeqfqwefqwe.qwefqweqwe")
    private String jwt;
}
