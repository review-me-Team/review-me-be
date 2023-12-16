package reviewme.be.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.feedback.request.PostFeedbackRequest;
import reviewme.be.feedback.response.PostFeedbackResponse;
import reviewme.be.question.request.PostQuestionRequest;
import reviewme.be.question.response.PostQuestionResponse;
import reviewme.be.util.CustomResponse;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Tag(name = "feedback", description = "피드백(feedback) API")
@RequestMapping("/resume/{resumeId}/feedback")
@RestController
@RequiredArgsConstructor
public class FeedbackController {

    @Operation(summary = "post feedback", description = "피드백을 추가합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 추가 성공"),
            @ApiResponse(responseCode = "400", description = "피드백 추가 실패")
    })
    public ResponseEntity<CustomResponse<PostFeedbackResponse>> postQuestions(@RequestBody PostFeedbackRequest postFeedbackRequest, @PathVariable long resumeId) {

        PostFeedbackResponse sampleResponse = PostFeedbackResponse.builder()
                .resumeId(1L)
                .writerId(1L)
                .writerName("aken-you")
                .writerProfileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                .resumePage(1L)
                .questionId(1L)
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

}
