package reviewme.be.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.feedback.repository.FeedbackEmojiRepository;
import reviewme.be.feedback.repository.FeedbackRepository;
import reviewme.be.feedback.request.PostFeedbackRequest;
import reviewme.be.feedback.request.UpdateFeedbackCheckRequest;
import reviewme.be.feedback.request.UpdateFeedbackContentRequest;
import reviewme.be.feedback.request.UpdateFeedbackEmojiRequest;
import reviewme.be.feedback.response.*;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.dto.Emoji;

import javax.persistence.Tuple;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "feedback", description = "피드백(feedback) API")
@RequestMapping("/resume/{resumeId}/feedback")
@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEmojiRepository feedbackEmojiRepository;

    @Operation(summary = "피드백 추가", description = "피드백을 추가합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 추가 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 추가 실패")
    })
    public ResponseEntity<CustomResponse<PostFeedbackResponse>> postFeedback(@RequestBody PostFeedbackRequest postFeedbackRequest, @PathVariable long resumeId) {

        PostFeedbackResponse sampleResponse = PostFeedbackResponse.builder()
                .resumeId(1L)
                .writerId(1L)
                .writerName("aken-you")
                .writerProfileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                .resumePage(1L)
                .feedbackId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "피드백 추가에 성공했습니다.",
                        sampleResponse
                ));
    }

    @Operation(summary = "피드백 목록 조회", description = "피드백 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<FeedbackPageResponse>> showFeedbacks(@PathVariable long resumeId, @PageableDefault(size=20) Pageable pageable, @RequestParam long resumePage) {

        // TODO: 본인의 resume인지 다른 사람의 resume인지에 따라 다른 데이터 응답 처리

        List<Emoji> emojis = feedbackEmojiRepository.countByFeedbackIdGroupByEmojiId(1L).stream()
                .map(tuple
                        -> Emoji.fromCountEmojiTuple(
                        tuple.get("id", Integer.class),
                        tuple.get("count", Long.class))
                ).collect(Collectors.toList());

        int myEmojiId = feedbackEmojiRepository.findByFeedbackIdAndUserId(1L, 1L)
                .getEmoji().getId();

        List<FeedbackResponse> feedbacksResponse = feedbackRepository.findByResumeIdAndResumePage(1, 1)
                .stream()
                .map(feedback -> FeedbackResponse.fromFeedbackOfOwnResume(feedback, emojis, myEmojiId))
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "피드백 목록 조회에 성공했습니다.",
                        FeedbackPageResponse.builder()
                                .feedbacks(feedbacksResponse)
                                .build()
                ));
    }

    @Operation(summary = "피드백에 달린 피드백(댓글) 목록 조회", description = "피드백에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/{feedbackId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 댓글 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<CommentOfFeedbackPageResponse>> showCommentsOfFeedback(@PathVariable long resumeId, @PathVariable long feedbackId, @PageableDefault(size=20) Pageable pageable) {

        // TODO: 본인의 resume인지 다른 사람의 resume인지에 따라 다른 데이터 응답 처리

        List<Emoji> sampleEmojis = List.of(
                Emoji.builder()
                        .id(1)
                        .count(10L)
                        .build(),
                Emoji.builder()
                        .id(2)
                        .count(3L)
                        .build());

        List<CommentOfFeedbackResponse> sampleResponse = List.of(
                CommentOfFeedbackResponse.builder()
                        .id(1L)
                        .feedbackId(1L)
                        .content("프로젝트에서 react-query를 사용하셨는데 사용한 이유가 궁금합니다.")
                        .writerId(1L)
                        .writerName("aken-you")
                        .writerProfileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                        .createdAt(LocalDateTime.now())
                        .emojiInfos(sampleEmojis)
                        .myEmojiId(1L)
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "피드백에 달린 댓글 조회에 성공했습니다.",
                        CommentOfFeedbackPageResponse.builder()
                                .comments(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "피드백 삭제", description = "피드백을 삭제합니다.")
    @DeleteMapping("/{feedbackId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 삭제 실패")
    })
    public ResponseEntity<CustomResponse> deleteFeedback(@PathVariable long resumeId, @PathVariable long feedbackId) {

        // TODO: 본인이 작성한 feedback만 삭제 가능

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "피드백 삭제에 성공했습니다."
                ));
    }

    @Operation(summary = "피드백 내용 수정", description = "피드백 내용을 수정합니다.")
    @PatchMapping("/{feedbackId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 수정 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 수정 실패")
    })
    public ResponseEntity<CustomResponse> updateFeedbackContent(@RequestBody UpdateFeedbackContentRequest updateFeedbackContentRequest, @PathVariable long resumeId, @PathVariable long feedbackId) {

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
    public ResponseEntity<CustomResponse> updateFeedbackCheck(@Valid @RequestBody UpdateFeedbackCheckRequest updateFeedbackCheckRequest, @PathVariable long resumeId, @PathVariable long feedbackId) {

        // TODO: 본인의 resume인지 검증, 맞다면 request 상태로 수정
        // TODO: feedback이 댓글이 아닌 feedback인 경우에만 체크 상태 수정 가능

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
    public ResponseEntity<CustomResponse> updateFeedbackEmoji(@RequestBody UpdateFeedbackEmojiRequest updateFeedbackEmojiRequest, @PathVariable long resumeId, @PathVariable long feedbackId) {

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "피드백 이모지 수정에 성공했습니다."
                ));
    }
}
