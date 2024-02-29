package reviewme.be.question.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.question.dto.QuestionCommentInfo;
import reviewme.be.util.dto.EmojiCount;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "예상 질문 대댓글 목록 응답")
public class QuestionCommentResponse {

    @Schema(description = "예상 질문 댓글 ID", example = "1")
    private Long id;

    @Schema(description = "예상 질문 ID", example = "1")
    private long parentQuestionId;

    @Schema(description = "댓글 내용", example = "흠.. 그러게요... 조금 더 공부해보겠습니다!")
    private String content;

    @Schema(description = "댓글 작성자 ID", example = "1")
    private long commenterId;

    @Schema(description = "댓글 작성자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "댓글 작성자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "댓글 작성 시간", example = "2024-01-02 01:33:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "이모지 정보")
    private List<EmojiCount> emojis;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private Integer myEmojiId;

    public static QuestionCommentResponse fromQuestionComment(QuestionCommentInfo questionComment,
        List<EmojiCount> emojis, Integer myEmojiId) {

        return QuestionCommentResponse.builder()
            .id(questionComment.getId())
            .parentQuestionId(questionComment.getParentQuestionId())
            .content(questionComment.getContent())
            .commenterId(questionComment.getCommenterId())
            .commenterName(questionComment.getCommenterName())
            .commenterProfileUrl(questionComment.getCommenterProfileUrl())
            .createdAt(questionComment.getCreatedAt())
            .emojis(emojis)
            .myEmojiId(myEmojiId)
            .build();
    }
}