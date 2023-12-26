package reviewme.be.resume.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "이력서 수정(제목, 공개범위) 요청")
public class UpdateResumeRequest {


    @Schema(description = "이력서 제목", example = "네이버 신입 개발자 준비")
    @NotBlank(message = "이력서 제목은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "이력서 공개 범위 ID", example = "1")
    @NotNull(message = "공개 범위 선택은 필수입니다.")
    private Integer scopeId;

    @Schema(description = "직군 ID", example = "1")
    @NotNull(message = "직군 선택은 필수입니다.")
    private Integer occupationId;

    @Schema(description = "재직 기간", example = "0")
    @NotNull(message = "재직 기간 선택은 필수입니다.")
    private Integer year;
}
