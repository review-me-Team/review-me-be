package reviewme.be.question.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예상 질문 체크 상태 수정 요청")
public class UpdateQuestionCheckRequest {

    @Schema(description = "예상 질문 체크 상태 수정 요청", example = "true")
    private boolean checked;
}
