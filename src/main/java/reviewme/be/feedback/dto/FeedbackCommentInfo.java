package reviewme.be.feedback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "피드백 대댓글 정보")
public class FeedbackCommentInfo {

    @Schema(description = "피드백 ID", example = "1")
    private long id;

    @Schema(description = "대댓글 부모 피드백 ID", example = "1")
    private long parentId;

    @Schema(description = "피드백 내용", example = "직접하셨던 내용들이 잘 드러난 것같습니다.")
    private String content;

    @Schema(description = "피드백을 단 사용자 ID", example = "1")
    private long commenterId;

    @Schema(description = "피드백을 단 사용자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "피드백을 단 사용자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15 09:27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @QueryProjection
    public FeedbackCommentInfo(long id, long parentId, String content, long commenterId, String commenterName,
        String commenterProfileUrl, LocalDateTime createdAt) {

        this.id = id;
        this.parentId = parentId;
        this.content = content;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterProfileUrl = commenterProfileUrl;
        this.createdAt = createdAt;
    }

}
