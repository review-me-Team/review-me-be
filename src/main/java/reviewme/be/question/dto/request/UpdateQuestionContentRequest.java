package reviewme.be.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예상 질문 내용 수정 요청")
public class UpdateQuestionContentRequest {

    @Schema(description = "예상 질문 수정 내용", example = "프로젝트에서 react-query말고 다른 방법은 없을까요?")
    @NotBlank(message = "예상 질문 내용은 필수 입력 값입니다.")
    private String content;
}
