package reviewme.be.friend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "친구 요청")
public class FollowFriendRequest {

    @Schema(description = "요청할 친구 ID", example = "1")
    @NotNull(message = "친구 ID는 필수 입력 값입니다.")
    private Long userId;
}
