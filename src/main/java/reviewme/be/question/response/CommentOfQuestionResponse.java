package reviewme.be.question.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.util.dto.EmojiInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "예상 질문 댓글 목록 응답")
public class CommentOfQuestionResponse {

    @Schema(description = "예상 질문 댓글 ID", example = "1")
    private long id;

    @Schema(description = "예상 질문 ID", example = "1")
    private long questionId;

    @Schema(description = "댓글 내용", example = "흠.. 그러게요... 조금 더 공부해보겠습니다!")
    private String content;

    @Schema(description = "댓글 작성자 ID", example = "1")
    private long writerId;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15")
    private LocalDateTime createdAt;

    @Schema(description = "이모지 정보")
    private List<EmojiInfo> emojiInfos;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private long myEmojiId;
}
