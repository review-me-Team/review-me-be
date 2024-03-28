package reviewme.be.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionsFilter {

    @Schema(description = "이력서 페이지", example = "1")
    @NotNull(message = "이력서 페이지는 필수 입력 값입니다.")
    private int resumePage;

    @Schema(description = "체크 여부", example = "true")
    private Boolean checked;

    @Schema(description = "북마크 여부", example = "true")
    private Boolean bookmarked;
}
