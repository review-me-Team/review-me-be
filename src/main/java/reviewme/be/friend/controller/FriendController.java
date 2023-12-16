package reviewme.be.friend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.friend.request.FollowFriendRequest;
import reviewme.be.util.CustomResponse;

@Tag(name = "friend", description = "친구(friend) API")
@RequestMapping("/friend")
@RestController
@RequiredArgsConstructor
public class FriendController {

    @Operation(summary = "친구 요청", description = "친구를 요청합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 성공"),
            @ApiResponse(responseCode = "400", description = "친구 요청 실패")
    })
    public ResponseEntity<CustomResponse> followFriend(@RequestBody FollowFriendRequest followFriendRequest) {

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 요청에 성공했습니다."
                ));
    }
}
