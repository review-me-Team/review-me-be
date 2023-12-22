package reviewme.be.resume.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.resume.entity.Resume;

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
    private long scopeId;

    @Schema(description = "직군 ID", example = "1")
    private int occupationId;

    @Schema(description = "년차", example = "0")
    private long year;

    public static ResumeResponse fromResume(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .writer(resume.getUser().getName())
                .createdAt(resume.getCreatedAt())
                .scopeId(resume.getScope().getId())
                .occupationId(resume.getOccupation().getId())
                .year(resume.getYear())
                .build();
    }
}
