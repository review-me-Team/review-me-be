package reviewme.be.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예상 질문 추가 요청")
public class PostQuestionRequest {

    @Schema(description = "예상 질문", example = "프로젝트에서 react-query를 사용하셨는데 왜 사용하셨나요?")
    @NotBlank(message = "예상 질문 내용은 필수 입력 값입니다.")
    private String content;

    @Schema(description = "댓글을 추가할 예상 질문 ID", example = "1", required = false)
    private Long questionId;

    @Schema(description = "라벨 ID", example = "1", required = false)
    private Long labelId;

    @Schema(description = "라벨 내용", example = "react-query", required = false)
    private String labelContent;

    @Schema(description = "이력서 페이지", example = "1")
    @NotNull(message = "이력서의 몇 페이지에 질문을 하는 지는 필수 입력 값입니다.")
    private int resumePage;
}
