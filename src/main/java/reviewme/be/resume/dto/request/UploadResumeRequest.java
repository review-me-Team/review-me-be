package reviewme.be.resume.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "이력서 업로드 요청")
public class UploadResumeRequest {

    @Schema(description = "이력서 제목", example = "네이버 신입 개발자 준비")
    @Max(value = 30, message = "이력서 제목은 최대 30자까지 입력 가능합니다.")
    @NotBlank(message = "이력서 제목은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "이력서 PDF 파일", example = "pdf 파일 객체를 주세요! (ex. resume.pdf) ")
    @NotNull(message = "PDF 파일은 필수 입력 값입니다.")
    private MultipartFile pdf;

    @Schema(description = "이력서 공개 범위 ID", example = "1")
    @NotBlank(message = "공개 범위 선택은 필수입니다.")
    private int scopeId;

    @Schema(description = "직군 ID", example = "1")
    @NotNull(message = "직군 선택은 필수입니다.")
    private int occupationId;

    @Schema(description = "년차", example = "0")
    @NotNull(message = "재직 년차 선택은 필수입니다.")
    private int year;
}
