package reviewme.be.friend.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.util.dto.UserInfo;

import java.util.List;

@Getter
@Builder
@Schema(description = "친구 목록 응답")
public class FriendsResponse {

    @Schema(description = "친구 정보 목록")
    private List<UserInfo> userInfos;
}
