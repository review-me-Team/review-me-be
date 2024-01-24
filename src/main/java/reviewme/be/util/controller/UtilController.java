package reviewme.be.util.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reviewme.be.custom.CustomResponse;
import reviewme.be.util.dto.response.*;
import reviewme.be.util.vo.EmojisVO;
import reviewme.be.util.vo.FeedbackLabelsVO;
import reviewme.be.util.vo.OccupationsVO;
import reviewme.be.util.vo.ScopesVO;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "util", description = "공통 API")
@RestController
@RequiredArgsConstructor
public class UtilController {

    private final EmojisVO emojisVO;
    private final FeedbackLabelsVO feedbackLabelsVO;
    private final ScopesVO scopesVO;
    private final OccupationsVO occupationsVO;

    @Operation(summary = "공개 범위 목록 조회", description = "공개 범위 목록을 조회합니다.")
    @GetMapping("/scope")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공개 범위 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "공개 범위 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<ScopePageResponse>> showScopes() {

        List<ScopeResponse> scopeResponses = scopesVO.getScopeList()
                .stream().map(ScopeResponse::fromScope)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "공개 범위 목록 조회에 성공했습니다.",
                        ScopePageResponse.builder()
                                .scopes(scopeResponses)
                                .build()
                ));
    }

    @Operation(summary = "이모지 목록 조회", description = "선택할 수 있는 이모지 목록을 조회합니다.")
    @GetMapping("/emoji")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이모지 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "이모지 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<EmojiPageResponse>> showEmojis() {

        List<EmojiResponse> emojiResponses = emojisVO.getEmojiList()
                .stream().map(EmojiResponse::fromEmoji)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이모지 목록 조회에 성공했습니다.",
                        EmojiPageResponse.builder()
                                .emojis(emojiResponses)
                                .build()
                ));
    }

    @Operation(summary = "직군 목록 조회", description = "직군 목록을 조회합니다.")
    @GetMapping("/occupation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "직군 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "직군 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<OccupationPageResponse>> showOccupations() {

        List<OccupationResponse> occupationResponses = occupationsVO.getOccupationList()
                .stream().map(OccupationResponse::fromOccupation)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "직군 목록 조회에 성공했습니다.",
                        OccupationPageResponse.builder()
                                .occupations(occupationResponses)
                                .build()
                ));
    }

    @Operation(summary = "피드백 라벨 목록 조회", description = "피드백 라벨 목록을 조회합니다.")
    @GetMapping("/label")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 라벨 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 라벨 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<LabelPageResponse>> showFeedbackLabels() {

        List<LabelResponse> labelsResponse = feedbackLabelsVO.getFeedbackLabelList()
                .stream()
                .map(LabelResponse::fromLabel)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "피드백 라벨 목록 조회에 성공했습니다.",
                        LabelPageResponse.builder()
                                .labels(labelsResponse)
                                .build()
                ));
    }
}
