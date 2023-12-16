package reviewme.be.friend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.friend.request.AcceptFriendRequest;
import reviewme.be.friend.request.FollowFriendRequest;
import reviewme.be.friend.response.FriendsResponse;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.dto.UserInfo;

import java.util.List;

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

    @Operation(summary = "친구 요청 수락", description = "친구 요청을 수락합니다.")
    @PatchMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 수락 성공"),
            @ApiResponse(responseCode = "400", description = "친구 요청 수락 실패")
    })
    public ResponseEntity<CustomResponse> acceptFriend(@RequestBody AcceptFriendRequest acceptFriendRequest) {

        // TODO: 친구 요청 목록에 있는지 검증

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 요청 수락에 성공했습니다."
                ));
    }

    @Operation(summary = "친구 목록 조회", description = "내 친구 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "친구 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<FriendsResponse>> showFriends(@PageableDefault(size=20) Pageable pageable) {

        List<UserInfo> sampleResponse = List.of(
                UserInfo.builder()
                        .id(1L)
                        .name("aken-you")
                        .profileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                        .build(),
                UserInfo.builder()
                        .id(2L)
                        .name("acceptor-gyu")
                        .profileUrl("https://avatars.githubusercontent.com/u/71162390?v=4")
                        .build()
        );

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 목록 조회에 성공했습니다.",
                        FriendsResponse.builder()
                                .userInfos(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "나에게 친구 요청 온 목록 조회", description = "나에게 친구 요청 온 목록을 조회합니다.")
    @GetMapping("/follower")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "친구 요청 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<FriendsResponse>> showFollowFriends(@PageableDefault(size=20) Pageable pageable) {

        // TODO: accepted: false인 친구 요청 목록 조회

        List<UserInfo> sampleResponse = List.of(
                UserInfo.builder()
                        .id(1L)
                        .name("aken-you")
                        .profileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                        .build(),
                UserInfo.builder()
                        .id(2L)
                        .name("acceptor-gyu")
                        .profileUrl("https://avatars.githubusercontent.com/u/71162390?v=4")
                        .build()
        );

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 요청 온 목록 조회에 성공했습니다.",
                        FriendsResponse.builder()
                                .userInfos(sampleResponse)
                                .count(2)
                                .build()
                ));
    }
}
