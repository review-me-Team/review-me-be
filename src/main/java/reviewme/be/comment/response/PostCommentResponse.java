package reviewme.be.comment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "추가된 댓글")
public class PostCommentResponse {

    @Schema(description = "이력서 ID", example = "1")
    private Long resumeId;

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "댓글 작성자 ID", example = "1")
    private Long commenterId;

    @Schema(description = "댓글 작성자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "댓글 작성자 프로필 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "피드백 작성 시간", example = "2023-11-23 09:27")
    private LocalDateTime createdAt;
}
