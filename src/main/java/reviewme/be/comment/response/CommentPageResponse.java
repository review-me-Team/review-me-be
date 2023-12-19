package reviewme.be.comment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.feedback.response.FeedbackResponse;

import java.util.List;

@Getter
@Builder
@Schema(description = "피드백 목록 응답")
public class CommentPageResponse {

    @Schema(description = "피드백 목록")
    private List<CommentResponse> comments;
}
