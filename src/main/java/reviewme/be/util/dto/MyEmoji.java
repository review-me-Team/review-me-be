package reviewme.be.util.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyEmoji {

    private Integer emojiId;

    private Long userId;

    /**
     * 사용자가 선택한 이모지가 없을 경우 null로 초기화
     */
    @QueryProjection
    public MyEmoji(Integer emojiId, Long userId) {

        this.emojiId = userId == null ? null : emojiId;
        this.userId = userId;
    }
}
