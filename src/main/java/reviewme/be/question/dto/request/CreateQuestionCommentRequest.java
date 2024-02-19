package reviewme.be.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateQuestionCommentRequest {

    @Schema(description = "예상 질문 대댓글 내용", example = "저도 그렇게 생각하던 중이었는데 감사합니다.")
    @NotBlank(message = "예상 질문 대댓글 내용은 필수 입력 값입니다.")
    private String content;
}
