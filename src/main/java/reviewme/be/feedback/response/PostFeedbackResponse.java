package reviewme.be.feedback.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "추가된 피드백")
public class PostFeedbackResponse {

    @Schema(description = "이력서 ID", example = "1")
    private long resumeId;

    @Schema(description = "피드백 작성자 ID", example = "1")
    private long writerId;

    @Schema(description = "피드백 작성자 이름", example = "aken-you")
    private String writerName;

    @Schema(description = "피드백 작성자 프로필 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String writerProfileUrl;

    @Schema(description = "피드백을 남긴 이력서의 페이지", example = "1")
    private long resumePage;

    @Schema(description = "피드백 ID", example = "1")
    private long feedbackId;

    @Schema(description = "피드백 작성 시간", example = "2023-11-23 09:27")
    private LocalDateTime createdAt;
}
