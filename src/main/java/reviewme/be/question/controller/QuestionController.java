package reviewme.be.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.question.request.PostQuestionRequest;
import reviewme.be.question.response.PostQuestionResponse;
import reviewme.be.question.response.QuestionPageResponse;
import reviewme.be.question.response.QuestionResponse;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.dto.EmojiInfo;

import java.time.LocalDateTime;
import java.util.List;

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
                .resumePage(1L)
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

    @Operation(summary = "question", description = "예상 질문 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<QuestionPageResponse>> postQuestions(@PathVariable long resumeId, @PageableDefault(size=20) Pageable pageable, @RequestParam long resumePage) {

        // TODO: 본인의 resume인지 다른 사람의 resume인지에 따라 다른 데이터 응답 처리

        List<EmojiInfo> sampleEmojis = List.of(
                EmojiInfo.builder()
                        .id(1L)
                        .countOfEmoji(10L)
                        .build(),
                EmojiInfo.builder()
                        .id(2L)
                        .countOfEmoji(3L)
                        .build());

        List<QuestionResponse> sampleResponse = List.of(
                QuestionResponse.builder()
                        .id(1L)
                        .content("프로젝트에서 react-query를 사용하셨는데 사용한 이유가 궁금합니다.")
                        .writerId(1L)
                        .labelId(1L)
                        .createdAt(LocalDateTime.now())
                        .countOfReplies(10L)
                        .isBookmarked(true)
                        .isChecked(true)
                        .emojiInfos(sampleEmojis)
                        .myEmojiId(1L)
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 목록 조회에 성공했습니다.",
                        QuestionPageResponse.builder()
                                .questionPage(sampleResponse)
                                .build()
                ));
    }
}
