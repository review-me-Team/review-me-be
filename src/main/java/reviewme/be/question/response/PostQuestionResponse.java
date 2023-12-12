package reviewme.be.question.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "예상 질문 업로드 응답")
public class PostQuestionResponse {

    @Schema(description = "이력서 ID", example = "1")
    private long resumeId;

    @Schema(description = "예상 질문 작성자 ID", example = "1")
    private long writerId;

    @Schema(description = "예상 질문 작성자 이름", example = "aken-you")
    private String writerName;

    @Schema(description = "예상 질문 작성자 프로필 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String writerProfileUrl;

    @Schema(description = "예상 질문 ID", example = "1")
    private long questionId;

    @Schema(description = "예상 질문 작성 시간", example = "2023-11-23 09:27")
    private LocalDateTime createdAt;

}
