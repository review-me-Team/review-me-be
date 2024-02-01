package reviewme.be.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "피드백 내용 수정 요청")
public class UpdateFeedbackContentRequest {

    @Schema(description = "피드백 수정 내용", example = "아직 문제 해결과 관련된 경험이 부족해서 사용자 중심의 서비스를 위한 내용을 위주로 구성해봤습니다.")
    @NotBlank(message = "피드백 내용은 필수 입력 값입니다.")
    private String content;
}
