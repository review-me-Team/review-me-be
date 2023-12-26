package reviewme.be.question.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "예상 질문 업로드 응답")
public class PostedQuestionResponse {

    @Schema(description = "이력서 ID", example = "1")
    private Long resumeId;

    @Schema(description = "예상 질문 작성자 ID", example = "1")
    private Long writerId;

    @Schema(description = "예상 질문 작성자 이름", example = "aken-you")
    private String writerName;

    @Schema(description = "예상 질문 작성자 프로필 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String writerProfileUrl;

    @Schema(description = "예상 질문 내용", example = "프로젝트에서 react-query를 사용하셨는데 왜 사용하셨나요?")
    private String content;

    @Schema(description = "예상 질문 라벨", example = "react-query")
    private String labelContent ;

    @Schema(description = "질문한 이력서의 페이지", example = "1")
    private Integer resumePage;

    @Schema(description = "예상 질문 ID", example = "1")
    private Long questionId;

    @Schema(description = "예상 질문 작성 시간", example = "2023-11-23 09:27")
    private LocalDateTime createdAt;

}
