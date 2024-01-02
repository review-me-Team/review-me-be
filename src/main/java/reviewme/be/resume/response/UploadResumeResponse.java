package reviewme.be.resume.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이력서 업로드 응답")
public class UploadResumeResponse {

    @Schema(description = "이력서 ID", example = "1")
    private Long id;

    public static UploadResumeResponse fromSavedResumeId(Long id) {
        return UploadResumeResponse.builder()
                .id(id)
                .build();
    }
}
