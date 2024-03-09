package reviewme.be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.custom.CustomResponse;
import reviewme.be.user.dto.UserGitHubProfile;
import reviewme.be.user.dto.UserGitHubToken;
import reviewme.be.user.dto.UserRefreshedToken;
import reviewme.be.user.dto.request.OAuthCodeRequest;
import reviewme.be.user.dto.response.LoginUserResponse;
import reviewme.be.user.dto.response.UserPageResponse;
import reviewme.be.user.dto.response.UserProfileResponse;
import reviewme.be.user.dto.response.UserResponse;
import reviewme.be.user.exception.NoRefreshTokenException;
import reviewme.be.user.exception.NoSearchConditionException;
import reviewme.be.user.service.JWTService;
import reviewme.be.user.service.OAuthLoginService;
import reviewme.be.user.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;

@Tag(name = "user", description = "사용자(user), 로그인(login) API")
@RequestMapping
@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuthLoginService oauthLoginService;
    private final JWTService jwtService;
    private final UserService userService;

    @Operation(summary = "GitHub으로 로그인", description = "GitHub 계정을 통해 사용자가 로그인합니다.")
    @PostMapping("/login/oauth")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    public ResponseEntity<CustomResponse<Void>> loginWithGitHub(
        @RequestBody OAuthCodeRequest request,
        HttpServletResponse response) {

        UserGitHubToken userGitHubToken = oauthLoginService.getUserGitHubToken(request.getCode());

        setRefreshToken(response, userGitHubToken.getRefreshToken());

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "로그인에 성공했습니다."
            ));
    }

    @Operation(summary = "사용자 토큰 새로고침", description = "기한이 만료된 토큰을 새로 만듭니다.")
    @GetMapping("/user/refresh")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "토큰 새로 받기 성공"),
        @ApiResponse(responseCode = "400", description = "토큰 새로 받기 실패")
    })
    public ResponseEntity<CustomResponse<LoginUserResponse>> getRefreshJwt(
        HttpServletRequest request,
        HttpServletResponse response) {

        // TODO: 사용자 정보 업데이트

        String refreshToken = findRefreshTokenFromRequest(request);

        UserRefreshedToken userRefreshedToken = oauthLoginService.getUserRefreshedToken(
            refreshToken);
        String jwt = createJwtByGitHubToken(userRefreshedToken.getAccessToken(), userRefreshedToken.getExpiresIn());

        setRefreshToken(response, userRefreshedToken.getRefreshToken());

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "로그인에 성공했습니다.",
                LoginUserResponse.builder()
                    .jwt(jwt)
                    .build()
            ));
    }

    @Operation(summary = "사용자 검색 목록 조회", description = "검색한 이름으로 시작하는 사용자 목록을 조회합니다.")
    @GetMapping("/user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 검색 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "사용자 검색 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<UserPageResponse>> showUsersStartsWith(
        @PageableDefault(size = 20) Pageable pageable,
        @RequestParam String start) {

        if (start == null || start.isEmpty()) {
            throw new NoSearchConditionException("검색할 이름을 입력해주세요.");
        }

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

    private String createJwtByGitHubToken(String accessToken, long expiresIn) {

        UserGitHubProfile userGitHubProfile = oauthLoginService.getUserGitHubProfile(accessToken);
        UserProfileResponse userProfileResponse = userService.getUserByGithubProfile(
            userGitHubProfile);

        return jwtService.createJwt(userProfileResponse, getJwtExpiredAt(expiresIn));
    }

    private static Date getJwtExpiredAt(long expiresIn) {

        long currentTime = System.currentTimeMillis();
        long twoWeeksInMillis = expiresIn * 1000L;

        return new Date(currentTime + twoWeeksInMillis);
    }

    private void setRefreshToken(HttpServletResponse response, String refreshToken) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(60 * 60 * 24 * 14)
            .sameSite("None")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private String findRefreshTokenFromRequest(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new NoRefreshTokenException("로그인이 필요합니다.");
        }

        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("refreshToken"))
            .findFirst()
            .orElseThrow(() -> new NoRefreshTokenException("로그인이 필요합니다."))
            .getValue();
    }
}
