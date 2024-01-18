package reviewme.be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.custom.CustomResponse;
import reviewme.be.user.dto.request.OAuthCodeRequest;
import reviewme.be.user.dto.response.UserPageResponse;
import reviewme.be.user.dto.response.UserProfileResponse;
import reviewme.be.user.dto.response.UserResponse;
import reviewme.be.user.service.OAuthLoginService;
import reviewme.be.user.service.UserService;

@Tag(name = "user", description = "사용자(user), 로그인(login) API")
@RequestMapping
@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuthLoginService oAuthLoginService;
    private final UserService userService;

    @Operation(summary = "GitHub으로 로그인", description = "GitHub 계정을 통해 사용자가 로그인합니다.")
    @PostMapping("/login/oauth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    public ResponseEntity<CustomResponse<UserProfileResponse>> loginWithGitHub(
            @RequestBody OAuthCodeRequest request) {

        UserProfileResponse sampleResponse = oAuthLoginService.getUserProfile(request.getCode());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "로그인에 성공했습니다.",
                        sampleResponse
                ));
    }


    @Operation(summary = "사용자 검색 목록 조회", description = "검색한 이름으로 시작하는 사용자 목록을 조회합니다.")
    @GetMapping("/user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 검색 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "사용자 검색 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<UserPageResponse>> showUserInfoStartsWith(@PageableDefault(size=20) Pageable pageable, @RequestParam String start) {

        Page<UserResponse> searchedUsers = userService.getUsersByStartName(start, pageable);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "검색한 이름으로 시작하는 사용자 목록을 조회에 성공했습니다.",
                        UserPageResponse.fromUserPageable(searchedUsers)
                ));
    }
}
