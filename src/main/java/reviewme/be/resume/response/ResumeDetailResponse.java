package reviewme.be.resume.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이력서 상세 내용 조회")
public class ResumeDetailResponse {

    @Schema(description = "이력서 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String resumeUrl;

    @Schema(description = "이력서 제목", example = "네이버 신입 개발자 준비")
    private String title;

    @Schema(description = "이력서 작성자 이름", example = "aken-you")
    private String writerName;

    @Schema(description = "직군", example = "Frontend")
    private String occupation;

    @Schema(description = "재직 기간", example = "0")
    private Integer year;
}
