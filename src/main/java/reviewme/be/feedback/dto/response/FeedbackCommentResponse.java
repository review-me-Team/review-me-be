package reviewme.be.feedback.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import reviewme.be.util.dto.EmojiCount;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "피드백 대댓글 목록 응답")
public class FeedbackCommentResponse {

    @Schema(description = "피드백 댓글 ID", example = "1")
    private long id;

    @Schema(description = "피드백 ID", example = "1")
    private long parentFeedbackId;

    @Schema(description = "댓글 내용", example = "저도 그렇게 느껴지긴 했는데 조금 더 보완해야겠네요. 감사합니다!")
    private String content;

    @Schema(description = "댓글 작성자 ID", example = "1")
    private long commenterId;

    @Schema(description = "댓글 작성자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "댓글 작성자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "이모지 정보")
    private List<EmojiCount> emojis;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private Integer myEmojiId;

    @QueryProjection
    public FeedbackCommentResponse(long id, long parentFeedbackId, String content, long commenterId, String commenterName,
        String commenterProfileUrl, LocalDateTime createdAt, Integer myEmojiId) {

        this.id = id;
        this.parentFeedbackId = parentFeedbackId;
        this.content = content;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterProfileUrl = commenterProfileUrl;
        this.createdAt = createdAt;
        this.myEmojiId = myEmojiId;
    }
}
