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
import reviewme.be.question.request.*;
import reviewme.be.question.response.*;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.dto.Emoji;
import reviewme.be.util.response.LabelPageResponse;
import reviewme.be.util.response.LabelResponse;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "question", description = "예상 질문(question) API")
@RequestMapping("/resume/{resumeId}/question")
@RestController
@RequiredArgsConstructor
public class QuestionController {

    @Operation(summary = "예상 질문 추가", description = "예상 질문을 추가합니다.")
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

    @Operation(summary = "예상 질문 목록 조회", description = "예상 질문 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<QuestionPageResponse>> showQuestions(@PathVariable long resumeId, @PageableDefault(size=20) Pageable pageable, @RequestParam long resumePage) {

        // TODO: 본인의 resume인지 다른 사람의 resume인지에 따라 다른 데이터 응답 처리

        List<Emoji> sampleEmojis = List.of(
                Emoji.builder()
                        .id(1L)
                        .count(10L)
                        .build(),
                Emoji.builder()
                        .id(2L)
                        .count(3L)
                        .build());

        List<QuestionResponse> sampleResponse = List.of(
                QuestionResponse.builder()
                        .id(1L)
                        .content("프로젝트에서 react-query를 사용하셨는데 사용한 이유가 궁금합니다.")
                        .writerId(1L)
                        .labelId(1L)
                        .createdAt(LocalDateTime.now())
                        .countOfReplies(10L)
                        .bookmarked(true)
                        .checked(true)
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
                                .questions(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "예상 질문에 달린 예상 질문(댓글) 목록 조회", description = "예상 질문에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/{questionId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 댓글 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<CommentOfQuestionPageResponse>> showCommentsOfQuestions(@PathVariable long resumeId, @PathVariable long questionId, @PageableDefault(size=20) Pageable pageable) {

        // TODO: 본인의 resume인지 다른 사람의 resume인지에 따라 다른 데이터 응답 처리

        List<Emoji> sampleEmojis = List.of(
                Emoji.builder()
                        .id(1L)
                        .count(10L)
                        .build(),
                Emoji.builder()
                        .id(2L)
                        .count(3L)
                        .build());

        List<CommentOfQuestionResponse> sampleResponse = List.of(
                CommentOfQuestionResponse.builder()
                        .id(1L)
                        .questionId(1L)
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
                        CommentOfQuestionPageResponse.builder()
                                .comments(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "예상 질문 삭제", description = "예상 질문을 삭제합니다.")
    @DeleteMapping("/{questionId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 삭제 실패")
    })
    public ResponseEntity<CustomResponse> deleteQuestions(@PathVariable long resumeId, @PathVariable long questionId) {

        // TODO: 본인이 작성한 question만 삭제 가능

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 삭제에 성공했습니다."
                ));
    }

    @Operation(summary = "예상 질문 내용 수정", description = "예상 질문 내용을 수정합니다.")
    @PatchMapping("/{questionId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 수정 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 수정 실패")
    })
    public ResponseEntity<CustomResponse> updateQuestionContent(@RequestBody UpdateQuestionContentRequest updateQuestionContentRequest, @PathVariable long resumeId, @PathVariable long questionId) {

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 수정에 성공했습니다."
                ));
    }

    @Operation(summary = "예상 질문 체크 상태 수정", description = "본인의 이력서에 대한 예상 질문 내용에 체크 상태를 수정합니다.")
    @PatchMapping("/{questionId}/check")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 체크 상태 수정 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 체크 상태 수정 실패")
    })
    public ResponseEntity<CustomResponse> updateQuestionCheck(@Valid @RequestBody UpdateQuestionCheckRequest updateQuestionCheckRequest, @PathVariable long resumeId, @PathVariable long questionId) {

        // TODO: 본인의 resume인지 검증, 맞다면 request 상태로 수정
        // TODO: question이 댓글이 아닌 question인 경우에만 체크 상태 수정 가능

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 체크 상태 수정에 성공했습니다."
                ));
    }

    @Operation(summary = "예상 질문 북마크 상태 수정", description = "본인의 이력서에 대한 예상 질문 내용에 북마크 상태를 수정합니다.")
    @PatchMapping("/{questionId}/bookmark")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 북마크 상태 수정 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 북마크 상태 수정 실패")
    })
    public ResponseEntity<CustomResponse> updateQuestionBookmark(@Valid @RequestBody UpdateQuestionBookmarkRequest updateQuestionBookmarkRequest, @PathVariable long resumeId, @PathVariable long questionId) {

        // TODO: 본인의 resume인지 검증, 맞다면 request 상태로 수정

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 북마크 상태 수정에 성공했습니다."
                ));
    }

    @Operation(summary = "예상 질문 라벨 목록 조회", description = "예상 질문 라벨 목록을 조회합니다.")
    @GetMapping("/label")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 라벨 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 라벨 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<LabelPageResponse>> showLabelsOfQuestions(@PathVariable long resumeId) {

        List<LabelResponse> sampleLabels = List.of(
                LabelResponse.builder()
                        .id(1L)
                        .label("react-query")
                        .build(),
                LabelResponse.builder()
                        .id(2L)
                        .label("typescript")
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 라벨 목록 조회에 성공했습니다.",
                        LabelPageResponse.builder()
                                .labels(sampleLabels)
                                .build()
                ));
    }

    @Operation(summary = "예상 질문 이모지 수정", description = "예상 질문에 표시할 이모지를 수정합니다.")
    @PatchMapping("/{questionId}/emoji")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예상 질문 이모지 상태 수정 성공"),
            @ApiResponse(responseCode = "400", description = "예상 질문 이모지 상태 수정 실패")
    })
    public ResponseEntity<CustomResponse> updateQuestionEmoji(@RequestBody UpdateQuestionEmojiRequest updateQuestionEmojiRequest, @PathVariable long resumeId, @PathVariable long questionId) {

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "예상 질문 이모지 수정에 성공했습니다."
                ));
    }
}
