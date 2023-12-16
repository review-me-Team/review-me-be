package reviewme.be.question.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.util.dto.EmojiInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "예상 질문 응답")
public class QuestionResponse {

    @Schema(description = "예상 질문 ID", example = "1")
    private long id;

    @Schema(description = "예상 질문 내용", example = "프로젝트에서 react-query를 사용하셨는데 사용한 이유가 궁금합니다.")
    private String content;

    @Schema(description = "질문자 ID", example = "1")
    private long writerId;

    @Schema(description = "예상 질문 라벨 ID", example = "1")
    private long labelId;

    @Schema(description = "예상 질문 작성 시간", example = "2023-12-15")
    private LocalDateTime createdAt;

    @Schema(description = "대댓글 개수", example = "10")
    private long countOfReplies;

    @Schema(description = "북마크 여부", example = "true")
    private boolean bookmarked;

    @Schema(description = "체크 여부", example = "true")
    private boolean checked;

    @Schema(description = "이모지 정보")
    private List<EmojiInfo> emojiInfos;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private long myEmojiId;
}
