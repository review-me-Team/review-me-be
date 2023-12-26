package reviewme.be.feedback.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.feedback.entity.Feedback;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "추가된 피드백")
public class PostedFeedbackResponse {

    @Schema(description = "피드백 ID", example = "2")
    private Long id;

    @Schema(description = "이력서 ID", example = "1")
    private Long resumeId;

    @Schema(description = "피드백 작성자 ID", example = "1")
    private Long writerId;

    @Schema(description = "피드백 작성자 이름", example = "aken-you")
    private String writerName;

    @Schema(description = "피드백 작성자 프로필 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String writerProfileUrl;

    @Schema(description = "피드백 내용", example = "뭔가 이력서에 문제 해결과 관련된 내용이 부족해보이는 것같아요.")
    private String content;

    @Schema(description = "피드백 라벨", example = "프로젝트")
    private String labelContent ;

    @Schema(description = "피드백을 남긴 이력서의 페이지", example = "1")
    private Integer resumePage;

    @Schema(description = "부모 피드백 ID", example = "1")
    private Long parentFeedbackId;

    @Schema(description = "피드백 작성 시간", example = "2023-11-23 09:27")
    private LocalDateTime createdAt;

    public static PostedFeedbackResponse fromFeedbackOfOwnResume(Feedback feedback) {

        return PostedFeedbackResponse.builder()
                .id(feedback.getId())
                .resumeId(feedback.getResume().getId())
                .writerId(feedback.getWriter().getId())
                .writerName(feedback.getWriter().getName())
                .writerProfileUrl(feedback.getWriter().getProfileUrl())
                .content(feedback.getContent())
                .labelContent(feedback.getLabel().getContent() == null ? null : feedback.getLabel().getContent())
                .resumePage(feedback.getResumePage())
                .parentFeedbackId(feedback.getParentFeedback().getId())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
