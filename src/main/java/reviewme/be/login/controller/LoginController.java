package reviewme.be.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.custom.CustomResponse;
import reviewme.be.login.request.OAuthCodeRequest;
import reviewme.be.login.response.UserProfileResponse;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Tag(name = "login", description = "로그인(login) API")
@RequestMapping("/login/oauth")
@RestController
@RequiredArgsConstructor
public class LoginController {

    @Operation(summary = "GitHub으로 로그인", description = "GitHub 계정을 통해 사용자가 로그인합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    public ResponseEntity<CustomResponse<UserProfileResponse>> loginWithGitHub(
            @RequestBody OAuthCodeRequest oAuthCodeRequest,
            HttpServletResponse response) {

        UserProfileResponse sampleResponse = UserProfileResponse.builder()
                .id(1L)
                .name("aken-you")
                .avatarUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                .build();

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "로그인에 성공했습니다.",
                        sampleResponse
                ));
    }
}
