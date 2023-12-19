package reviewme.be.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomResponse<T> {

    @Schema(description = "성공 여부", example = "success")
    private String status;

    @Schema(description = "응답 코드", example = "200")
    private int code;

    @Schema(description = "응답 메시지", example = "요청 성공 여부 등")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public CustomResponse(String status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
