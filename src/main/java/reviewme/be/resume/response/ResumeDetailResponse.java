package reviewme.be.resume.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "이력서 상세 내용 조회")
public class ResumeDetailResponse {

    @Schema(description = "이력서 url", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String resumeUrl;
}
