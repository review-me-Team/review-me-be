package reviewme.be.util.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "직군 목록 페이지 응답")
public class OccupationResponse {

    @Schema(description = "직군 ID", example = "1")
    private Long id;

    @Schema(description = "직군", example = "frontend")
    private String occupation;
}