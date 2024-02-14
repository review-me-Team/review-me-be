package reviewme.be.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateFeedbackCommentRequest {

    @Schema(description = "피드백 대댓글 내용", example = "저도 그렇게 생각하던 중이었는데 감사합니다.")
    @NotBlank(message = "피드백 대댓글 내용은 필수 입력 값입니다.")
    private String content;
}
