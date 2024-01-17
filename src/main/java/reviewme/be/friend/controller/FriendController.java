package reviewme.be.friend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reviewme.be.friend.request.AcceptFriendRequest;
import reviewme.be.friend.request.FollowFriendRequest;
import reviewme.be.friend.response.UserPageResponse;
import reviewme.be.custom.CustomResponse;
import reviewme.be.friend.service.FriendService;
import reviewme.be.user.dto.UserResponse;

@Tag(name = "friend", description = "친구(friend) API")
@RequestMapping("/friend")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 개발 편의성을 위해 로그인 기능 구현 전 userId를 1로 고정
    private long userId = 1L;

    @Operation(summary = "친구 요청", description = "친구를 요청합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 성공"),
            @ApiResponse(responseCode = "400", description = "친구 요청 실패")
    })
    public ResponseEntity<CustomResponse> followFriend(@Validated @RequestBody FollowFriendRequest followFriendRequest) {

        friendService.requestFriend(userId, followFriendRequest.getUserId());

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
    public ResponseEntity<CustomResponse> acceptFriend(@Validated @RequestBody AcceptFriendRequest acceptFriendRequest) {

        friendService.acceptFriend(userId, acceptFriendRequest.getUserId());

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
    public ResponseEntity<CustomResponse<UserPageResponse>> showFriends(@PageableDefault(size=20) Pageable pageable) {

        Page<UserResponse> friends = friendService.getFriends(userId, pageable);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 목록 조회에 성공했습니다.",
                        UserPageResponse.fromUserPageable(friends)
                ));
    }

    @Operation(summary = "나에게 친구 요청 온 목록 조회", description = "나에게 친구 요청 온 목록을 조회합니다.")
    @GetMapping("/follower")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "친구 요청 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<UserPageResponse>> showFollowFriends(@PageableDefault(size=20) Pageable pageable) {

        Page<UserResponse> friendRequests = friendService.getFriendRequests(userId, pageable);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 요청 온 목록 조회에 성공했습니다.",
                        UserPageResponse.fromUserPageable(friendRequests)
                ));
    }

    @Operation(summary = "사용자 검색 목록 조회", description = "검색한 이름으로 시작하는 사용자 목록을 조회합니다.")
    @GetMapping("/user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 검색 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "사용자 검색 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<UserPageResponse>> showUserInfoStartsWith(@PageableDefault(size=20) Pageable pageable, @RequestParam String start) {


        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "검색한 이름으로 시작하는 사용자 목록을 조회에 성공했습니다."
                ));
    }

    @Operation(summary = "친구 삭제", description = "친구를 삭제합니다.")
    @DeleteMapping("/{friendId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "친구 삭제 실패")
    })
    public ResponseEntity<CustomResponse> deleteFriend(@PathVariable long friendId) {

        friendService.deleteFriend(userId, friendId);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 삭제에 성공했습니다."
                ));
    }

    @Operation(summary = "친구 요청 거절", description = "친구 요청을 거절합니다.")
    @PatchMapping("/{friendId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 거절 성공"),
            @ApiResponse(responseCode = "400", description = "친구 요청 거절 실패")
    })
    public ResponseEntity<CustomResponse> rejectFriendRequest(@PathVariable long friendId) {

        friendService.cancelFriendRequest(userId, friendId);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "친구 요청 거절에 성공했습니다."
                ));
    }
}
