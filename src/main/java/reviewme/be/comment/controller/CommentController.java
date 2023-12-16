package reviewme.be.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.comment.request.PostCommentRequest;
import reviewme.be.comment.response.CommentPageResponse;
import reviewme.be.comment.response.CommentResponse;
import reviewme.be.comment.response.PostCommentResponse;
import reviewme.be.util.CustomResponse;
import reviewme.be.util.dto.EmojiInfo;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "commend", description = "댓글(comment) API")
@RequestMapping("/resume/{resumeId}/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    @Operation(summary = "POST comment of resume", description = "이력서에 대한 댓글을 추가합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 추가 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 추가 실패")
    })
    public ResponseEntity<CustomResponse<PostCommentResponse>> postCommentOfResume(@RequestBody PostCommentRequest postCommentRequest, @PathVariable long resumeId) {

        PostCommentResponse sampleResponse = PostCommentResponse.builder()
                .resumeId(1L)
                .commentId(1L)
                .commenterId(1L)
                .commenterName("aken-you")
                .commenterProfileUrl("https://avatars.githubusercontent.com/u/96980857?v=4")
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


    @Operation(summary = "GET comments of resume", description = "이력서에 달린 댓글 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<CommentPageResponse>> showCommentsOfResume(@PathVariable long resumeId, @PageableDefault(size=20) Pageable pageable) {

        List<EmojiInfo> sampleEmojis = List.of(
                EmojiInfo.builder()
                        .id(1L)
                        .count(10L)
                        .build(),
                EmojiInfo.builder()
                        .id(2L)
                        .count(3L)
                        .build());

        List<CommentResponse> sampleResponse = List.of(
                CommentResponse.builder()
                        .id(1L)
                        .content("전반적으로 이력서를 읽기가 편한 것같아요")
                        .commenterId(1L)
                        .createdAt(LocalDateTime.now())
                        .emojiInfos(sampleEmojis)
                        .myEmojiId(1L)
                        .build(),
                CommentResponse.builder()
                        .id(2L)
                        .content("이력서가 너무 길어요")
                        .commenterId(2L)
                        .createdAt(LocalDateTime.now())
                        .emojiInfos(sampleEmojis)
                        .myEmojiId(2L)
                        .build()
        );

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이력서에 달린 댓글 목록 조회에 성공했습니다.",
                        CommentPageResponse.builder()
                                .comments(sampleResponse)
                                .build()
                ));
    }

    @Operation(summary = "DELETE comment of resume", description = "이력서에 단 댓글을 삭제합니다.")
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

}
