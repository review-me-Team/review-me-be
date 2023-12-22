package reviewme.be.util.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이모지 정보 응답")
public class Emoji {

    @Schema(description = "이모지 ID", example = "1")
    private Integer id;

    @Schema(description = "이모지 개수", example = "10")
    private Long count;

    public static Emoji fromCountEmojiTuple(int id, long count) {
        return Emoji.builder()
                .id(id)
                .count(count)
                .build();
    }
}
