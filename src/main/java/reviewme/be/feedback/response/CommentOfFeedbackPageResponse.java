package reviewme.be.feedback.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "피드백 댓글 목록 응답")
public class CommentOfFeedbackPageResponse {

    @Schema(description = "피드백 댓글 목록")
    private List<CommentOfFeedbackResponse> comments;
}
