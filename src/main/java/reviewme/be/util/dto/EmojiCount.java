package reviewme.be.util.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이모지 정보 응답")
public class EmojiCount {

    @Schema(description = "이모지 ID", example = "1")
    private int id;

    @Schema(description = "이모지 개수", example = "10")
    private long count;

    @QueryProjection
    public EmojiCount(int id, long count) {
        this.id = id;
        this.count = count;
    }
}
