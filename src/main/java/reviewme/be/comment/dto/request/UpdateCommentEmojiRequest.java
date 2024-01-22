package reviewme.be.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "댓글 이모지 수정 요청")
public class UpdateCommentEmojiRequest {

    @Schema(description = "댓글 이모지 ID", example = "1")
    @NotNull(message = "댓글 이모지 ID는 필수 입력 값입니다.")
    private Long id;
}
