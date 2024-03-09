package reviewme.be.question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.question.dto.QuestionCommentInfo;
import reviewme.be.question.dto.QuestionInfo;

public interface QuestionRepositoryCustom {

    Page<QuestionInfo> findQuestionsByResumeIdAndResumePage(long resumeId, long userId, int resumePage, Pageable pageable);

    Page<QuestionCommentInfo> findQuestionCommentsByQuestionId(long questionId, long userId, Pageable pageable);
}
