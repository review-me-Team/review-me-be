package reviewme.be.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "댓글 추가 요청")
public class PostCommentRequest {

    @Schema(description = "댓글 내용", example = "전반적으로 이력서를 읽기가 편한 것같아요")
    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    private String content;
}
