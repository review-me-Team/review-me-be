package reviewme.be.resume.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "이력서 목록 응답")
public class ResumePageResponse {

    @Schema(description = "이력서 목록")
    private List<ResumeResponse> resumes;

    public static ResumePageResponse fromResumes(List<ResumeResponse> resumes) {
        return ResumePageResponse.builder()
                .resumes(resumes)
                .build();
    }
}
