package reviewme.be.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.question.request.PostQuestionRequest;
import reviewme.be.question.response.PostQuestionResponse;
import reviewme.be.util.CustomResponse;

import java.time.LocalDateTime;

@Tag(name = "question", description = "예상 질문(question) API")
@RequestMapping("/resume/{resumeId}/question")
@RestController
@RequiredArgsConstructor
public class QuestionController {

    @Operation(summary = "question", description = "예상 질문을 추가합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 추가 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 추가 실패")
    })
    public ResponseEntity<CustomResponse<PostQuestionResponse>> postQuestions(@RequestBody PostQuestionRequest postQuestionRequest, @PathVariable long resumeId) {

        PostQuestionResponse sampleResponse = PostQuestionResponse.builder()
                .resumeId(1L)
                .writerId(1L)
                .writerName("aken-you")
                .writerProfileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                .questionId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 추가에 성공했습니다.",
                        sampleResponse
                ));
    }
}
