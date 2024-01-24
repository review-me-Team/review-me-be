package reviewme.be.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.comment.dto.CommentInfo;
import reviewme.be.util.dto.EmojiCount;

import java.util.List;

@Getter
@Builder
@Schema(description = "댓글 정보 응답")
public class CommentResponse {

    @Schema(description = "댓글 정보")
    private CommentInfo comment;

    @Schema(description = "이모지 정보")
    private List<EmojiCount> emojis;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private Integer myEmojiId;

    public static CommentResponse fromComment(CommentInfo comment, List<EmojiCount> emojis, Integer myEmojiId) {

        return CommentResponse.builder()
                .comment(comment)
                .emojis(emojis)
                .myEmojiId(myEmojiId)
                .build();
    }
}
