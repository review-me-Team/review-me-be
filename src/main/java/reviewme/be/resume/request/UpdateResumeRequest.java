package reviewme.be.resume.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "공개 범위 선택은 필수입니다.")
    private Long scopeId;
}
