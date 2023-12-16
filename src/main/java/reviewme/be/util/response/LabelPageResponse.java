package reviewme.be.util.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "예상 질문 라벨 목록 응답")
public class LabelPageResponse {

    @Schema(description = "예상 질문 라벨 목록")
    private List<LabelResponse> labels;
}
