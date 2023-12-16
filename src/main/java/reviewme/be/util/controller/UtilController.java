package reviewme.be.util.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.response.*;

import java.util.List;

@Tag(name = "util", description = "공통 API")
@RestController
@RequiredArgsConstructor
public class UtilController {

    @Operation(summary = "scope", description = "공개 범위 목록을 조회합니다.")
    @GetMapping("/scope")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공개 범위 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "공개 범위 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<ScopePageResponse>> showScopes() {

        List<ScopeResponse> sampleResponse = List.of(
                ScopeResponse.builder()
                        .id(1L)
                        .scope("public")
                        .build(),
                ScopeResponse.builder()
                        .id(2L)
                        .scope("private")
                        .build(),
                ScopeResponse.builder()
                        .id(3L)
                        .scope("friend")
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "공개 범위 목록 조회에 성공했습니다.",
                        ScopePageResponse.builder()
                                .scopes(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "emoji", description = "선택할 수 있는 이모지 목록을 조회합니다.")
    @GetMapping("/emoji")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이모지 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "이모지 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<EmojiPageResponse>> showEmojis() {

        List<EmojiResponse> sampleResponse = List.of(
                EmojiResponse.builder()
                        .id(1L)
                        .emoji("🤔")
                        .build(),
                EmojiResponse.builder()
                        .id(2L)
                        .emoji("👍")
                        .build(),
                EmojiResponse.builder()
                        .id(3L)
                        .emoji("👀")
                        .build(),
                EmojiResponse.builder()
                        .id(4L)
                        .emoji("😎")
                        .build(),
                EmojiResponse.builder()
                        .id(5L)
                        .emoji("🙏")
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이모지 목록 조회에 성공했습니다.",
                        EmojiPageResponse.builder()
                                .emojis(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "occupation", description = "직군 목록을 조회합니다.")
    @GetMapping("/occupation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직군 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "직군 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<OccupationPageResponse>> showOccupations() {

        List<OccupationResponse> sampleResponse = List.of(
                OccupationResponse.builder()
                        .id(1L)
                        .occupation("Frontend")
                        .build(),
                OccupationResponse.builder()
                        .id(2L)
                        .occupation("Backend")
                        .build(),
                OccupationResponse.builder()
                        .id(3L)
                        .occupation("Android")
                        .build(),
                OccupationResponse.builder()
                        .id(4L)
                        .occupation("iOS")
                        .build(),
                OccupationResponse.builder()
                        .id(5L)
                        .occupation("AI/ML")
                        .build(),
                OccupationResponse.builder()
                        .id(6L)
                        .occupation("Data Engineering")
                        .build()
                );

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "직군 목록 조회에 성공했습니다.",
                        OccupationPageResponse.builder()
                                .occupations(sampleResponse)
                                .build()
                ));
    }
}
