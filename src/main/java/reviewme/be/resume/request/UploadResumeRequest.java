package reviewme.be.resume.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "이력서 업로드 요청")
public class UploadResumeRequest {

    @Schema(description = "이력서 제목", example = "네이버 신입 개발자 준비")
    @NotBlank(message = "이력서 제목은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "이력서 공개 범위 ID", example = "1")
    @NotBlank(message = "공개 범위 선택은 필수입니다.")
    private Long scopeId;

    @Schema(description = "직군 ID", example = "1")
    @NotNull(message = "직군 선택은 필수입니다.")
    private Long occupationId;

    @Schema(description = "년차", example = "0")
    @NotNull(message = "재직 년차 선택은 필수입니다.")
    private Long year;
}
