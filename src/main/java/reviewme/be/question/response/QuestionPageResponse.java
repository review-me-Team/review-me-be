package reviewme.be.question.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "예상 질문 목록 응답")
public class QuestionPageResponse {


    @Schema(description = "예상 질문 목록")
    private List<QuestionResponse> questions;
}
