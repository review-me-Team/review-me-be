package reviewme.be.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "피드백 체크 상태 수정 요청")
public class UpdateFeedbackCheckRequest {

    @Schema(description = "피드백 체크 상태 수정 요청", example = "true")
    @NotNull(message = "체크 상태는 필수 입력 값입니다.")
    private Boolean checked;
}
