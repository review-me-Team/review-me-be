package reviewme.be.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reviewme.be.feedback.dto.request.*;
import reviewme.be.feedback.dto.response.*;
import reviewme.be.custom.CustomResponse;
import reviewme.be.feedback.service.FeedbackService;
import reviewme.be.user.entity.User;

@Tag(name = "feedback", description = "피드백(feedback) API")
@RequestMapping("/resume/{resumeId}/feedback")
@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "피드백 추가", description = "피드백을 추가합니다.")
    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백 추가 성공"),
        @ApiResponse(responseCode = "400", description = "피드백 추가 실패")
    })
    public ResponseEntity<CustomResponse<Void>> postFeedback(
        @Validated @RequestBody CreateFeedbackRequest createFeedbackRequest,
        @PathVariable long resumeId,
        @RequestAttribute("user") User user) {

        feedbackService.saveFeedback(createFeedbackRequest, user, resumeId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백 추가에 성공했습니다."
            ));
    }

    @Operation(summary = "피드백에 대댓글 추가", description = "피드백에 대댓글을 추가합니다.")
    @PostMapping("/{feedbackId}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백에 대댓글 추가 성공"),
        @ApiResponse(responseCode = "400", description = "피드백에 대댓글 추가 실패")
    })
    public ResponseEntity<CustomResponse<Void>> postFeedbackComment(
        @Validated @RequestBody CreateFeedbackCommentRequest createFeedbackCommentRequest,
        @PathVariable long resumeId,
        @PathVariable long feedbackId,
        @RequestAttribute("user") User user) {

        feedbackService.saveFeedbackComment(createFeedbackCommentRequest, resumeId, user,
            feedbackId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백 대댓글 추가에 성공했습니다."
            ));
    }

    @Operation(summary = "피드백 목록 조회", description = "피드백 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "피드백 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<FeedbackPageResponse>> showFeedbacks(
        @PathVariable long resumeId,
        @RequestParam int resumePage,
        @RequestAttribute("user") User user,
        @PageableDefault(size = 20) Pageable pageable) {

        FeedbackPageResponse feedbacks = feedbackService.getFeedbacks(resumeId, resumePage, user,
            pageable);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백 목록 조회에 성공했습니다.",
                feedbacks
            ));
    }

    @Operation(summary = "피드백에 달린 피드백(댓글) 목록 조회", description = "피드백에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/{feedbackId}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백 댓글 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "피드백 댓글 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<FeedbackCommentPageResponse>> showCommentsOfFeedback(
        @PathVariable long resumeId,
        @PathVariable long feedbackId,
        @RequestAttribute("user") User user,
        @PageableDefault(size = 20) Pageable pageable) {

        FeedbackCommentPageResponse feedbackComments = feedbackService
            .getFeedbackComments(resumeId, feedbackId, user, pageable);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백에 달린 대댓글 조회에 성공했습니다.",
                feedbackComments
            ));
    }

    @Operation(summary = "피드백 삭제", description = "피드백을 삭제합니다.")
    @DeleteMapping("/{feedbackId}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "피드백 삭제 실패")
    })
    public ResponseEntity<CustomResponse<Void>> deleteFeedback(
        @PathVariable long resumeId,
        @PathVariable long feedbackId,
        @RequestAttribute("user") User user) {

        feedbackService.deleteFeedback(resumeId, feedbackId, user);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백 삭제에 성공했습니다."
            ));
    }

    @Operation(summary = "피드백 수정", description = "피드백을 수정합니다.")
    @PatchMapping("/{feedbackId}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백 수정 성공"),
        @ApiResponse(responseCode = "400", description = "피드백 수정 실패")
    })
    public ResponseEntity<CustomResponse<Void>> updateFeedbackContent(
        @Validated @RequestBody UpdateFeedbackContentRequest updateFeedbackContentRequest,
        @RequestAttribute("user") User user,
        @PathVariable long resumeId,
        @PathVariable long feedbackId) {

        feedbackService.updateFeedbackContent(updateFeedbackContentRequest, resumeId,
            feedbackId, user);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백 수정에 성공했습니다."
            ));
    }

    @Operation(summary = "피드백 체크 상태 수정", description = "본인의 이력서에 대한 피드백의 체크 상태를 수정합니다.")
    @PatchMapping("/{feedbackId}/check")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백 체크 상태 수정 성공"),
        @ApiResponse(responseCode = "400", description = "피드백 체크 상태 수정 실패")
    })
    public ResponseEntity<CustomResponse<Void>> updateFeedbackCheck(
        @Validated @RequestBody UpdateFeedbackCheckRequest updateFeedbackCheckRequest,
        @PathVariable long resumeId,
        @PathVariable long feedbackId,
        @RequestAttribute("user") User user) {

        feedbackService.updateFeedbackCheck(updateFeedbackCheckRequest, user, resumeId, feedbackId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백 체크 상태 수정에 성공했습니다."
            ));
    }

    @Operation(summary = "피드백 이모지 수정", description = "피드백에 표시할 이모지를 수정합니다.")
    @PatchMapping("/{feedbackId}/emoji")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "피드백 이모지 수정 성공"),
        @ApiResponse(responseCode = "400", description = "피드백 이모지 수정 실패")
    })
    public ResponseEntity<CustomResponse<Void>> updateFeedbackEmoji(
        @RequestBody UpdateFeedbackEmojiRequest updateFeedbackEmojiRequest,
        @PathVariable long resumeId,
        @PathVariable long feedbackId,
        @RequestAttribute("user") User user) {

        feedbackService.updateFeedbackEmoji(updateFeedbackEmojiRequest, resumeId, feedbackId, user);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(
                "success",
                200,
                "피드백 이모지 수정에 성공했습니다."
            ));
    }
}
