package reviewme.be.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviewme.be.comment.request.PostCommentRequest;
import reviewme.be.comment.response.PostCommentResponse;
import reviewme.be.util.CustomResponse;

import java.time.LocalDateTime;

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
    public ResponseEntity<CustomResponse<PostCommentResponse>> postFeedback(@RequestBody PostCommentRequest postCommentRequest, @PathVariable long resumeId) {

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
}
