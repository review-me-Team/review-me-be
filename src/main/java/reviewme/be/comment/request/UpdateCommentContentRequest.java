package reviewme.be.comment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "댓글 내용 수정 요청")
public class UpdateCommentContentRequest {

    @Schema(description = "댓글 내용", example = "무엇을 말하고 싶은지 한 눈에 들어와서 좋아요.")
    @NotBlank(message = "피드백 내용은 필수 입력 값입니다.")
    private String content;
}
