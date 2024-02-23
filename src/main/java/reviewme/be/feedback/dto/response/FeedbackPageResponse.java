package reviewme.be.feedback.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "피드백 목록 응답")
public class FeedbackPageResponse {

    @Schema(description = "피드백 목록")
    private List<FeedbackResponse> feedbacks;

    @Schema(description = "현재 페이지", example = "1")
    private int pageNumber;

    @Schema(description = "페이징 적용 시 전체 페이지 수", example = "10")
    private int lastPage;

    @Schema(description = "페이징 적용 시 한 번에 받아오는 데이터 개수", example = "20")
    private int pageSize;
}
