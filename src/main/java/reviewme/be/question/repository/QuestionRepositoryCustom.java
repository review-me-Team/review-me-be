package reviewme.be.question.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.question.dto.QuestionCommentInfo;
import reviewme.be.question.dto.QuestionInfo;
import reviewme.be.question.entity.Question;

public interface QuestionRepositoryCustom {

    Page<QuestionInfo> findQuestionsByResumeIdAndResumePage(long resumeId, long userId, int resumePage, Pageable pageable);

    Page<QuestionCommentInfo> findQuestionCommentsByQuestionId(long questionId, long userId, Pageable pageable);

    Optional<Question> findParentQuestionByIdAndResumeId(long questionId, long resumeId);
}
