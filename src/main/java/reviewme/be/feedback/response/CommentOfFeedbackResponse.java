package reviewme.be.feedback.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.util.dto.EmojiCount;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "피드백 댓글 목록 응답")
public class CommentOfFeedbackResponse {

    @Schema(description = "피드백 댓글 ID", example = "1")
    private Long id;

    @Schema(description = "피드백 ID", example = "1")
    private Long feedbackId;

    @Schema(description = "댓글 내용", example = "저도 그렇게 느껴지긴 했는데 조금 더 보완해야겠네요. 감사합니다!")
    private String content;

    @Schema(description = "댓글 작성자 이름", example = "aken-you")
    private String writerName;

    @Schema(description = "댓글 작성자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String writerProfileUrl;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "이모지 정보")
    private List<EmojiCount> emojis;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private Long myEmojiId;
}
