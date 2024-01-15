package reviewme.be.resume.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "이력서 목록 응답")
public class ResumePageResponse {

    @Schema(description = "이력서 목록")
    private List<ResumeResponse> resumes;

    // TODO: 회의 후 FE에서 필요한 정보 추가

    public static ResumePageResponse fromResumePageable(Page<ResumeResponse> resumePage) {
        return ResumePageResponse.builder()
                .resumes(resumePage.getContent())
                .build();
    }
}
