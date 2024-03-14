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
import reviewme.be.user.dto.response.UserPageResponse;
import reviewme.be.custom.CustomResponse;
import reviewme.be.friend.service.FriendService;
import reviewme.be.user.dto.response.UserResponse;
import reviewme.be.user.entity.User;

@Tag(name = "friend", description = "친구(friend) API")
@RequestMapping("/friend")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "친구 요청", description = "친구를 요청합니다.")
    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "친구 요청 성공"),
        @ApiResponse(responseCode = "400", description = "친구 요청 실패")
    })
    public ResponseEntity<CustomResponse<Void>> followFriend(
        @Validated @RequestBody FollowFriendRequest followFriendRequest,
        @RequestAttribute("user") User user) {

        friendService.requestFriend(followFriendRequest, user);

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
    public ResponseEntity<CustomResponse<Void>> acceptFriend(
        @Validated @RequestBody AcceptFriendRequest acceptFriendRequest,
        @RequestAttribute("user") User user) {

        friendService.acceptFriend(acceptFriendRequest, user);

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
    public ResponseEntity<CustomResponse<UserPageResponse>> showFriends(
        @PageableDefault(size = 20) Pageable pageable,
        @RequestAttribute("user") User user) {

        Page<UserResponse> friends = friendService.getFriends(user, pageable);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "친구 목록 조회에 성공했습니다.",
                UserPageResponse.fromUserPageable(friends)
            ));
    }

    @Operation(summary = "나에게 온 친구 요청 목록 조회", description = "나에게 온 친구 요청 목록을 조회합니다.")
    @GetMapping("/follower")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "친구 요청 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "친구 요청 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<UserPageResponse>> showFollowFriends(
        @PageableDefault(size = 20) Pageable pageable,
        @RequestAttribute("user") User user) {

        Page<UserResponse> receivedFriendRequests = friendService.getReceivedFriendRequests(user, pageable);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "나에게 온 친구 요청 목록 조회에 성공했습니다.",
                UserPageResponse.fromUserPageable(receivedFriendRequests)
            ));
    }

    @Operation(summary = "내가 보낸 친구 요청 목록 조회", description = "내가 보낸 친구 요청 목록을 조회합니다.")
    @GetMapping("/following")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "내가 보낸 친구 요청 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "내가 보낸 친구 요청 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<UserPageResponse>> showFollowingFriends(
        @PageableDefault(size = 20) Pageable pageable,
        @RequestAttribute("user") User user) {

        Page<UserResponse> sentFriendRequests = friendService.getSentFriendRequests(user, pageable);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "내가 보낸 친구 요청 목록 조회에 성공했습니다.",
                UserPageResponse.fromUserPageable(sentFriendRequests)
            ));
    }

    @Operation(summary = "친구 삭제", description = "친구를 삭제합니다.")
    @DeleteMapping("/{friendId}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "친구 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "친구 삭제 실패")
    })
    public ResponseEntity<CustomResponse<Void>> deleteFriend(
        @PathVariable long friendId,
        @RequestAttribute("user") User user) {

        friendService.deleteFriend(friendId, user);

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
    public ResponseEntity<CustomResponse> rejectFriendRequest(
        @PathVariable long friendId,
        @RequestAttribute("user") User user) {

        friendService.cancelFriendRequest(friendId, user);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "친구 요청 거절에 성공했습니다."
            ));
    }
}
