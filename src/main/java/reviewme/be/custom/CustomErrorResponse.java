package reviewme.be.custom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomErrorResponse {

    @Schema(description = "성공 여부", example = "fail")
    private String status;

    @Schema(description = "응답 코드", example = "400")
    private int code;

    @Schema(description = "응답 메시지", example = "요청 실패 원인 등")
    private String message;

    @Schema(description = "응답 데이터")
    private Object data = null;

    public CustomErrorResponse(String status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
