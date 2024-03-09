package reviewme.be.feedback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "피드백 정보")
public class FeedbackInfo {

    @Schema(description = "피드백 ID", example = "1")
    private long id;

    @Schema(description = "피드백 내용", example = "직접하셨던 내용들이 잘 드러난 것같습니다.")
    private String content;

    @Schema(description = "피드백 라벨", example = "프로젝트")
    private String labelContent;

    @Schema(description = "피드백을 단 사용자 ID", example = "1")
    private long commenterId;

    @Schema(description = "피드백을 단 사용자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "피드백을 단 사용자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15 09:27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "해당 피드백에 달린 대댓글 개수", example = "10")
    private long countOfReplies;

    @Schema(description = "체크 여부", example = "true")
    private boolean checked;

    @Schema(description = "내가 선택한 이모지 Id", example = "1")
    private Integer myEmojiId;

    @QueryProjection
    public FeedbackInfo(long id, String content, String labelContent, long commenterId, String commenterName,
        String commenterProfileUrl, LocalDateTime createdAt, long countOfReplies, boolean checked, Integer myEmojiId) {
        this.id = id;
        this.content = content;
        this.labelContent = labelContent;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterProfileUrl = commenterProfileUrl;
        this.createdAt = createdAt;
        this.countOfReplies = countOfReplies;
        this.checked = checked;
        this.myEmojiId = myEmojiId;
    }
}
