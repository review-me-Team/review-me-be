package reviewme.be.resume.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "이력서 목록 페이지 응답")
public class ResumeResponse {

    @Schema(description = "이력서 ID", example = "1")
    private Long id;

    @Schema(description = "이력서 제목", example = "네이버 신입 개발자 준비")
    private String title;

    @Schema(description = "이력서 작성자 이름", example = "aken-you")
    private String writer;

    @Schema(description = "이력서 작성 시간", example = "2023-11-22")
    private LocalDateTime createdAt;

    @Schema(description = "공개 범위", example = "1")
    private Long scopeId;
}
