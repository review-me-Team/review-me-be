package reviewme.be.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예상 질문 북마크 상태 수정 요청")
public class UpdateQuestionBookmarkRequest {

    @Schema(description = "예상 질문 북마크 상태 수정 요청", example = "true")
    @NotNull(message = "북마크 상태는 필수 입력 값입니다.")
    private Boolean bookmarked;
}
