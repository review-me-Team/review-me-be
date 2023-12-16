package reviewme.be.comment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.util.dto.EmojiInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "댓글 정보 응답")
public class CommentResponse {

    @Schema(description = "댓글 ID", example = "1")
    private long id;

    @Schema(description = "댓글 내용", example = "전반적으로 이력서를 읽기가 편한 것같아요")
    private String content;

    @Schema(description = "댓글 단 사용자 ID", example = "1")
    private long commenterId;

    @Schema(description = "댓글 작성 시간", example = "2023-12-15")
    private LocalDateTime createdAt;;

    @Schema(description = "이모지 정보")
    private List<EmojiInfo> emojiInfos;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private long myEmojiId;
}
