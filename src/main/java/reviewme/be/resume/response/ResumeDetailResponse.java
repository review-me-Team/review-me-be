package reviewme.be.resume.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.resume.entity.Resume;

@Getter
@Builder
@Schema(description = "이력서 상세 내용 조회")
public class ResumeDetailResponse {

    @Schema(description = "이력서 url", example = "9a6498f5%E1%84%8B%E1%85%B5%E1%84%85%E1%85%A7%E1%86%A8%E1%84%89%E1%85%A5_%E1%84%89%E1%85%A2%E1%86%B7%E1%84%91%E1%85%B3%E1%86%AF.pdf")
    private String resumeUrl;

    @Schema(description = "이력서 제목", example = "네이버 신입 개발자 준비")
    private String title;

    @Schema(description = "이력서 작성자 이름", example = "aken-you")
    private String writerName;

    @Schema(description = "직군", example = "Frontend")
    private String occupation;

    @Schema(description = "재직 기간", example = "0")
    private Integer year;

    public static ResumeDetailResponse fromResume(Resume resume) {
        return ResumeDetailResponse.builder()
                .resumeUrl(resume.getUrl())
                .title(resume.getTitle())
                .writerName(resume.getUser().getName())
                .occupation(resume.getOccupation().getOccupation())
                .year(resume.getYear())
                .build();
    }
}
