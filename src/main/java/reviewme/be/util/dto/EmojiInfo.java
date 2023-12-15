package reviewme.be.util.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이모지 정보 응답")
public class EmojiInfo {

    @Schema(description = "이모지 ID", example = "1")
    private long id;

    @Schema(description = "이모지 개수", example = "10")
    private long countOfEmoji;
}
