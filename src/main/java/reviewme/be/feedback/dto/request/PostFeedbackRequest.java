package reviewme.be.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "피드백 추가 요청")
public class PostFeedbackRequest {

    @Schema(description = "피드백 내용", example = "뭔가 이력서에 문제 해결과 관련된 내용이 부족해보이는 것같아요.")
    @NotBlank(message = "피드백 내용은 필수 입력 값입니다.")
    private String content;

    @Schema(description = "댓글을 추가할 피드백 ID", example = "1", required = false)
    private Long feedbackId;

    @Schema(description = "피드백 라벨 ID", example = "1", required = false)
    private Long labelId;

    @Schema(description = "이력서 페이지", example = "1")
    @NotNull(message = "이력서의 몇 페이지에 질문을 하는 지는 필수 입력 값입니다.")
    private Integer resumePage;
}
