package reviewme.be.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "피드백 추가 요청")
public class CreateFeedbackRequest {

    @Schema(description = "피드백 내용", example = "뭔가 이력서에 문제 해결과 관련된 내용이 부족해보이는 것같아요.")
    @NotBlank(message = "피드백 내용은 필수 입력 값입니다.")
    private String content;

    @Schema(description = "피드백 라벨 ID", example = "1", required = false)
    private Long labelId;

    @Schema(description = "이력서 페이지", example = "1")
    @Min(value = 1, message = "이력서의 페이지는 1페이지부터 시작합니다.")
    private int resumePage;
}
