package reviewme.be.util.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이모지 목록 응답")
public class EmojiResponse {

    @Schema(description = "이모지 ID", example = "1")
    private Long id;

    @Schema(description = "이모지", example = "🤔")
    private String emoji;
}
