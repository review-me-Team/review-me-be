package reviewme.be.util.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "공개 범위 목록 페이지 응답")
public class ScopeResponse {

    @Schema(description = "공개 범위 ID", example = "1")
    private Long id;

    @Schema(description = "공개 범위", example = "public")
    private String scope;
}
