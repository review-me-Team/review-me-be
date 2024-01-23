package reviewme.be.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "댓글 정보")
public class CommentInfo {

    @Schema(description = "댓글 ID", example = "1")
    private Long id;

    @Schema(description = "댓글 내용", example = "전반적으로 이력서를 읽기가 편한 것같아요!")
    private String content;

    @Schema(description = "댓글 단 사용자 ID", example = "1")
    private Long commenterId;

    @Schema(description = "댓글 단 사용자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "댓글 단 사용자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15 09:27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @QueryProjection
    public CommentInfo(Long id, String content, Long commenterId, String commenterName, String commenterProfileUrl, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterProfileUrl = commenterProfileUrl;
        this.createdAt = createdAt;
    }
}
