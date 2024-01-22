package reviewme.be.util.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "공개 범위 목록 응답")
public class ScopePageResponse {

    @Schema(description = "공개 범위 목록")
    private List<ScopeResponse> scopes;
}
