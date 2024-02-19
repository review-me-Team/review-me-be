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
import reviewme.be.comment.dto.request.PostCommentRequest;
import reviewme.be.comment.dto.request.UpdateCommentContentRequest;
import reviewme.be.comment.dto.request.UpdateCommentEmojiRequest;
import reviewme.be.comment.dto.response.CommentPageResponse;
import reviewme.be.comment.service.CommentService;
import reviewme.be.custom.CustomResponse;
import reviewme.be.user.entity.User;


@Tag(name = "comment", description = "댓글(comment) API")
@RequestMapping("/resume/{resumeId}/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 추가", description = "이력서에 대한 댓글을 추가합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 추가 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 추가 실패")
    })
    public ResponseEntity<CustomResponse<Void>> postCommentOfResume(
            @Validated @RequestBody PostCommentRequest postCommentRequest,
            @PathVariable long resumeId,
            @RequestAttribute("user") User user) {

        commentService.saveComment(user, resumeId, postCommentRequest);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "댓글 추가에 성공했습니다."
                ));
    }


    @Operation(summary = "댓글 목록 조회", description = "이력서에 달린 댓글 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<CommentPageResponse>> showCommentsOfResume(
            @PathVariable long resumeId,
            @PageableDefault(size = 20) Pageable pageable) {

        long userId = 1L;

        CommentPageResponse comments = commentService.getComments(userId, resumeId, pageable);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이력서에 달린 댓글 목록 조회에 성공했습니다.",
                        comments
                ));
    }

    @Operation(summary = "댓글 삭제", description = "이력서에 단 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 삭제 실패")
    })
    public ResponseEntity<CustomResponse<Void>> deleteCommentOfResume(
            @PathVariable long resumeId,
            @PathVariable long commentId,
            @RequestAttribute("user") User user) {

        commentService.deleteComment(user, resumeId, commentId);

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
    public ResponseEntity<CustomResponse<Void>> updateCommentContent(
            @Validated @RequestBody UpdateCommentContentRequest updateCommentContentRequest,
            @PathVariable long resumeId,
            @PathVariable long commentId,
            @RequestAttribute("user") User user) {

        commentService.updateComment(updateCommentContentRequest, user, resumeId, commentId);

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
    public ResponseEntity<CustomResponse<Void>> updateCommentEmoji(@RequestBody UpdateCommentEmojiRequest updateCommentEmojiRequest, @PathVariable long resumeId, @PathVariable long commentId) {

        long userId = 1L;
        commentService.updateCommentEmoji(userId, commentId, updateCommentEmojiRequest);

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "댓글 이모지 수정에 성공했습니다."
                ));
    }
}
