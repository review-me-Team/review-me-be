package reviewme.be.feedback.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.util.dto.Emoji;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "피드백 댓글 목록 응답")
public class CommentOfFeedbackResponse {

    @Schema(description = "피드백 댓글 ID", example = "1")
    private long id;

    @Schema(description = "피드백 ID", example = "1")
    private long feedbackId;

    @Schema(description = "댓글 내용", example = "저도 그렇게 느껴지긴 했는데 조금 더 보완해야겠네요. 감사합니다!")
    private String content;

    @Schema(description = "댓글 작성자 ID", example = "1")
    private long writerId;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15")
    private LocalDateTime createdAt;

    @Schema(description = "이모지 정보")
    private List<Emoji> emojiInfos;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private long myEmojiId;
}
