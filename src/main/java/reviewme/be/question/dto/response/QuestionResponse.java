package reviewme.be.question.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.question.dto.QuestionInfo;
import reviewme.be.question.entity.Question;
import reviewme.be.util.dto.EmojiCount;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "예상 질문 응답")
public class QuestionResponse {

    @Schema(description = "예상 질문 ID", example = "1")
    private Long id;

    @Schema(description = "예상 질문 내용", example = "프로젝트에서 react-query를 사용하셨는데 사용한 이유가 궁금합니다.")
    private String content;

    @Schema(description = "질문자 ID", example = "1")
    private long commenterId;

    @Schema(description = "질문자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "질문자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "예상 질문 라벨", example = "react-query")
    private String labelContent;

    @Schema(description = "예상 질문 작성 시간", example = "2024-01-02 01:32")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "대댓글 개수", example = "10")
    private long countOfReplies;

    @Schema(description = "체크 여부", example = "true")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean checked;

    @Schema(description = "북마크 여부", example = "true")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean bookmarked;

    @Schema(description = "이모지 정보")
    private List<EmojiCount> emojis;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private Integer myEmojiId;

    public static QuestionResponse fromQuestionOfOwnResume(QuestionInfo question,
        List<EmojiCount> emojis, Integer myEmojiId) {

        return QuestionResponse.builder()
            .id(question.getId())
            .content(question.getContent())
            .commenterId(question.getCommenterId())
            .commenterName(question.getCommenterName())
            .commenterProfileUrl(question.getCommenterProfileUrl())
            .labelContent(question.getLabelContent())
            .createdAt(question.getCreatedAt())
            .countOfReplies(question.getCountOfReplies())
            .checked(question.isChecked())
            .bookmarked(question.isBookmarked())
            .emojis(emojis)
            .myEmojiId(myEmojiId)
            .build();
    }

    public static QuestionResponse fromQuestionOfOtherResume(QuestionInfo question,
        List<EmojiCount> emojis, Integer myEmojiId) {

        return QuestionResponse.builder()
            .id(question.getId())
            .content(question.getContent())
            .commenterId(question.getCommenterId())
            .commenterName(question.getCommenterName())
            .commenterProfileUrl(question.getCommenterProfileUrl())
            .labelContent(question.getLabelContent())
            .createdAt(question.getCreatedAt())
            .countOfReplies(question.getCountOfReplies())
            .emojis(emojis)
            .myEmojiId(myEmojiId)
            .build();
    }
}
