package reviewme.be.util.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "예상 질문 라벨 목록 페이지 응답")
public class LabelResponse {

    @Schema(description = "예상 질문 라벨 ID", example = "1")
    private Long id;

    @Schema(description = "예상 질문 라벨", example = "react-query")
    private String label;
}
