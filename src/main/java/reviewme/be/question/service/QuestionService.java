package reviewme.be.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.question.entity.Question;
import reviewme.be.question.exception.NonExistQuestionException;
import reviewme.be.question.repository.QuestionRepository;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.util.entity.Label;
import reviewme.be.util.repository.LabelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ResumeService resumeService;
    private final LabelRepository labelRepository;

    @Transactional
    public void deleteQuestion(long resumeId, long questionId, User user) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 질문 존재 여부 및 삭제 권한 확인
        Question question = findById(questionId);
        question.validateUser(user);

        question.softDelete();
    }

    @Transactional
    public List<Label> findQuestionLabels(long resumeId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        return labelRepository.findByResumeId(resumeId);
    }

    private Question findById(long questionId) {

        return questionRepository.findByIdAndDeletedAtIsNull(questionId)
                .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 질문입니다."));
    }
}
