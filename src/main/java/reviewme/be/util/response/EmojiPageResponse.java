package reviewme.be.util.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "이모지 목록 응답")
public class EmojiPageResponse {

    @Schema(description = "이모지 목록")
    private List<EmojiResponse> emojis;
}
