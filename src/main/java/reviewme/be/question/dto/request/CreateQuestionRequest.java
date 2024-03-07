package reviewme.be.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예상 질문 추가 요청")
public class CreateQuestionRequest {

    @Schema(description = "예상 질문", example = "프로젝트에서 react-query를 사용하셨는데 왜 사용하셨나요?")
    @NotBlank(message = "예상 질문 내용은 필수 입력 값입니다.")
    private String content;

    @Schema(description = "라벨 내용", example = "react-query", required = false)
    @Length(max = 20, message = "라벨 내용은 20자 이하여야 합니다.")
    private String labelContent;

    @Schema(description = "이력서 페이지", example = "1")
    @NotNull(message = "이력서의 몇 페이지에 질문을 하는 지는 필수 입력 값입니다.")
    private int resumePage;
}
