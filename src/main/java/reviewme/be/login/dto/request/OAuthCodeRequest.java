package reviewme.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "OAuth code 요청")
public class OAuthCodeRequest {

    @Schema(description = "code", example = "c844fd8c6a65aa72e7fc")
    @NotBlank(message = "code 내용은 필수 입력 값입니다.")
    private String code;
}
