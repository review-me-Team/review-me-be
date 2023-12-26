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
    private String writerName;

    @Schema(description = "이력서 작성자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String writerProfileUrl;

    @Schema(description = "이력서 작성 시간", example = "2023-11-22")
    private LocalDateTime createdAt;

    @Schema(description = "공개 범위", example = "public")
    private String scope;

    @Schema(description = "직군", example = "1")
    private String occupation;

    @Schema(description = "년차", example = "0")
    private Integer year;

    public static ResumeResponse fromResume(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .writerName(resume.getUser().getName())
                .writerProfileUrl(resume.getUser().getProfileUrl())
                .createdAt(resume.getCreatedAt())
                .scope(resume.getScope().getScope())
                .occupation(resume.getOccupation().getOccupation())
                .year(resume.getYear())
                .build();
    }
}
