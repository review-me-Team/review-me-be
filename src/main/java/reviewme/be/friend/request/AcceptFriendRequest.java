package reviewme.be.friend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "친구 요청 수락")
public class AcceptFriendRequest {

    @Schema(description = "수락할 친구의 ID", example = "1")
    private Long userId;
}
