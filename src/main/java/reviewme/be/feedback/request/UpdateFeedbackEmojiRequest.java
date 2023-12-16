package reviewme.be.feedback.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "피드백 이모지 수정 요청")
public class UpdateFeedbackEmojiRequest {

    @Schema(description = "피드백 이모지 ID", example = "1")
    private Long id;
}
