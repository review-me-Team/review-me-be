package reviewme.be.question.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예상 질문 추가 요청")
public class PostQuestionRequest {

    @Schema(description = "예상 질문", example = "프로젝트에서 react-query를 사용하셨는데 왜 사용하셨나요?")
    @NotBlank(message = "예상 질문 내용은 필수 입력 값입니다.")
    private String content;

    @Schema(description = "라벨 내용", example = "react-query")
    private String labelContent;
}
