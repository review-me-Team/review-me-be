package reviewme.be.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예상 질문 이모지 수정 요청")
public class UpdateQuestionEmojiRequest {

    @Schema(description = "예상 질문 이모지 ID", example = "1")
    private Integer id;
}
