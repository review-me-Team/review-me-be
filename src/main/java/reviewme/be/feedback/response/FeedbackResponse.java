package reviewme.be.feedback.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.util.dto.EmojiInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "피드백 정보 응답")
public class FeedbackResponse {

    @Schema(description = "피드백 ID", example = "1")
    private long id;

    @Schema(description = "피드백 내용", example = "뭔가 이력서에 문제 해결과 관련된 내용이 부족한 것같아요.")
    private String content;

    @Schema(description = "피드백을 남긴 사용자 ID", example = "1")
    private long writerId;

    @Schema(description = "피드백 라벨 ID", example = "1")
    private long labelId;

    @Schema(description = "피드백 작성 시간", example = "2023-12-15")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 개수", example = "10")
    private long countOfReplies;

    @Schema(description = "체크 여부", example = "true")
    private boolean checked;

    @Schema(description = "이모지 정보")
    private List<EmojiInfo> emojiInfos;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private long myEmojiId;
}
