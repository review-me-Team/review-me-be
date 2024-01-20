package reviewme.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "친구 목록 응답")
public class UserPageResponse {

    @Schema(description = "친구 정보 목록")
    private List<UserResponse> users;

    @Schema(description = "전체 사용자 수", example = "10")
    private long numOfUsers;

    @Schema(description = "현재 페이지", example = "1")
    private int pageNumber;

    @Schema(description = "전체 페이지 수", example = "1")
    private int lastPage;

    @Schema(description = "한 번에 받아오는 데이터 개수", example = "10")
    private int pageSize;

    public static UserPageResponse fromUserPageable(Page<UserResponse> userPage) {

        return UserPageResponse.builder()
                .users(userPage.getContent())
                .numOfUsers(userPage.getTotalElements())
                .pageNumber(userPage.getNumber())
                .lastPage(userPage.getTotalPages() - 1)
                .pageSize(userPage.getSize())
                .build();
    }
}
