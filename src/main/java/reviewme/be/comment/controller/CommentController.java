package reviewme.be.comment.controller;

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
import reviewme.be.comment.entity.CommentEmoji;
import reviewme.be.comment.repository.CommentEmojiRepository;
import reviewme.be.comment.repository.CommentRepository;
import reviewme.be.comment.request.PostCommentRequest;
import reviewme.be.comment.request.UpdateCommentContentRequest;
import reviewme.be.comment.request.UpdateCommentEmojiRequest;
import reviewme.be.comment.response.CommentPageResponse;
import reviewme.be.comment.response.CommentResponse;
import reviewme.be.comment.response.PostedCommentResponse;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.dto.Emoji;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "comment", description = "댓글(comment) API")
@RequestMapping("/resume/{resumeId}/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentEmojiRepository commentEmojiRepository;

    @Operation(summary = "댓글 추가", description = "이력서에 대한 댓글을 추가합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 추가 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 추가 실패")
    })
    public ResponseEntity<CustomResponse<PostedCommentResponse>> postCommentOfResume(@Validated @RequestBody PostCommentRequest postCommentRequest, @PathVariable long resumeId) {

        PostedCommentResponse sampleResponse = PostedCommentResponse.builder()
                .resumeId(1L)
                .commentId(1L)
                .commenterId(1L)
                .commenterName("aken-you")
                .commenterProfileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
                .content(postCommentRequest.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "댓글 추가에 성공했습니다.",
                        sampleResponse
                ));
    }


    @Operation(summary = "댓글 목록 조회", description = "이력서에 달린 댓글 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<CommentPageResponse>> showCommentsOfResume(@PathVariable long resumeId, @PageableDefault(size=20) Pageable pageable) {

        List<Emoji> emojis = commentEmojiRepository.countByCommentIdGroupByEmojiId(2L)
                .stream()
                .map(tuple -> Emoji.fromCountEmojiTuple(
                        tuple.get("id", Integer.class),
                        tuple.get("count", Long.class))
                ).collect(Collectors.toList());

        CommentEmoji myCommentEmoji = commentEmojiRepository.findByCommentIdAndUserId(2L, 1L);
        int myEmojiId = myCommentEmoji == null ? 0 : myCommentEmoji.getEmoji().getId();

        List<CommentResponse> commentsResponse = commentRepository.findByResumeIdOrderByCreatedAtDesc(resumeId)
                .stream()
                .map(comment -> CommentResponse.fromComment(comment, emojis, myEmojiId))
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이력서에 달린 댓글 목록 조회에 성공했습니다.",
                        CommentPageResponse.builder()
                                .comments(commentsResponse)
                                .build()
                ));
    }

    @Operation(summary = "댓글 삭제", description = "이력서에 단 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 삭제 실패")
    })
    public ResponseEntity<CustomResponse> deleteCommentOfResume(@PathVariable long resumeId, @PathVariable long commentId) {

        // TODO: 본인이 작성한 댓글만 삭제 가능

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "댓글 삭제에 성공했습니다."
                ));
    }

    @Operation(summary = "댓글 수정", description = "이력서에 단 댓글 내용을 수정합니다.")
    @PatchMapping("/{commentId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패")
    })
    public ResponseEntity<CustomResponse> updateCommentContent(@Validated @RequestBody UpdateCommentContentRequest updateCommentContentRequest, @PathVariable long resumeId, @PathVariable long commentId) {

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "댓글 수정에 성공했습니다."
                ));
    }

    @Operation(summary = "댓글 이모지 수정", description = "댓글에 표시할 이모지를 수정합니다.")
    @PatchMapping("/{commentId}/emoji")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 이모지 수정 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 이모지 수정 실패")
    })
    public ResponseEntity<CustomResponse> updateCommentEmoji(@Validated @RequestBody UpdateCommentEmojiRequest updateCommentEmojiRequest, @PathVariable long resumeId, @PathVariable long commentId) {

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "댓글 이모지 수정에 성공했습니다."
                ));
    }
}
