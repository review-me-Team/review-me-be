package reviewme.be.feedback.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "피드백 목록 응답")
public class FeedbackPageResponse {

    @Schema(description = "피드백 목록")
    private List<FeedbackResponse> feedbackPage;
}
