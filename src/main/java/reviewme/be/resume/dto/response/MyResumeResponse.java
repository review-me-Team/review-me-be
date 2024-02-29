package reviewme.be.resume.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.resume.entity.Resume;

@Getter
@Builder
@Schema(description = "이력서 목록 페이지 응답")
public class MyResumeResponse {

    @Schema(description = "이력서 ID", example = "1")
    private Long id;

    @Schema(description = "이력서 제목", example = "네이버 신입 개발자 준비")
    private String title;

    @Schema(description = "이력서 작성 시간", example = "2024-01-02 01:32")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "공개 범위", example = "public")
    private String scope;

    @Schema(description = "직군", example = "1")
    private String occupation;

    @Schema(description = "년차", example = "0")
    private Integer year;

    public static MyResumeResponse fromResume(Resume resume) {

        return MyResumeResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .createdAt(resume.getCreatedAt())
                .scope(resume.getScope().getScope())
                .occupation(resume.getOccupation().getOccupation())
                .year(resume.getYear())
                .build();
    }

    @QueryProjection
    public MyResumeResponse(Long id, String title, LocalDateTime createdAt, String scope, String occupation, Integer year) {

        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.scope = scope;
        this.occupation = occupation;
        this.year = year;
    }
}
