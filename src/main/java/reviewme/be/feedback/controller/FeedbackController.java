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
import reviewme.be.feedback.request.PostFeedbackRequest;
import reviewme.be.feedback.response.*;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.dto.EmojiInfo;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "feedback", description = "피드백(feedback) API")
@RequestMapping("/resume/{resumeId}/feedback")
@RestController
@RequiredArgsConstructor
public class FeedbackController {

    @Operation(summary = "POST feedback", description = "피드백을 추가합니다.")
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

    @Operation(summary = "GET feedbacks", description = "피드백 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<FeedbackPageResponse>> showFeedbacks(@PathVariable long resumeId, @PageableDefault(size=20) Pageable pageable, @RequestParam long resumePage) {

        // TODO: 본인의 resume인지 다른 사람의 resume인지에 따라 다른 데이터 응답 처리

        List<EmojiInfo> sampleEmojis = List.of(
                EmojiInfo.builder()
                        .id(1L)
                        .count(10L)
                        .build(),
                EmojiInfo.builder()
                        .id(2L)
                        .count(3L)
                        .build());

        List<FeedbackResponse> sampleResponse = List.of(
                FeedbackResponse.builder()
                        .id(1L)
                        .content("뭔가 이력서에 문제 해결과 관련된 내용이 부족한 것같아요.")
                        .writerId(1L)
                        .labelId(1L)
                        .createdAt(LocalDateTime.now())
                        .countOfReplies(10L)
                        .checked(true)
                        .emojiInfos(sampleEmojis)
                        .myEmojiId(1L)
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "피드백 목록 조회에 성공했습니다.",
                        FeedbackPageResponse.builder()
                                .feedbackPage(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "GET comments of feedback", description = "피드백에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/{feedbackId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 댓글 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<CommentOfFeedbackPageResponse>> showCommentsOfFeedback(@PathVariable long resumeId, @PathVariable long feedbackId, @PageableDefault(size=20) Pageable pageable) {

        // TODO: 본인의 resume인지 다른 사람의 resume인지에 따라 다른 데이터 응답 처리

        List<EmojiInfo> sampleEmojis = List.of(
                EmojiInfo.builder()
                        .id(1L)
                        .count(10L)
                        .build(),
                EmojiInfo.builder()
                        .id(2L)
                        .count(3L)
                        .build());

        List<CommentOfFeedbackResponse> sampleResponse = List.of(
                CommentOfFeedbackResponse.builder()
                        .id(1L)
                        .feedbackId(1L)
                        .content("프로젝트에서 react-query를 사용하셨는데 사용한 이유가 궁금합니다.")
                        .writerId(1L)
                        .createdAt(LocalDateTime.now())
                        .emojiInfos(sampleEmojis)
                        .myEmojiId(1L)
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 댓글 조회에 성공했습니다.",
                        CommentOfFeedbackPageResponse.builder()
                                .comments(sampleResponse)
                                .build()
                ));
    }

}
