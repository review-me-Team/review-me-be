package reviewme.be.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.question.dto.request.PostQuestionRequest;
import reviewme.be.question.dto.request.UpdateQuestionBookmarkRequest;
import reviewme.be.question.dto.request.UpdateQuestionCheckRequest;
import reviewme.be.question.dto.request.UpdateQuestionContentRequest;
import reviewme.be.question.entity.Question;
import reviewme.be.question.exception.NonExistQuestionException;
import reviewme.be.question.repository.QuestionRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.util.entity.Label;
import reviewme.be.util.repository.LabelRepository;
import reviewme.be.util.service.UtilService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ResumeService resumeService;
    private final LabelRepository labelRepository;
    private final UtilService utilService;

    @Transactional
    public void saveQuestion(PostQuestionRequest request, long resumeId, User user) {

        // 이력서 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);

        // 예상 질문 라벨 조회
        Label label = verifyQuestionLabel(request, resume);

        if (request.getQuestionId() != null) {

            Question parentQuestion = findParentQuestion(request.getQuestionId(), resumeId, request.getResumePage());
            questionRepository.save(Question.createChildQuestion(user, resume, parentQuestion, request.getContent(), request.getResumePage()));
            parentQuestion.plusChildCnt();
            return;
        }

        questionRepository.save(Question.createQuestion(user, resume, label, request.getContent(), request.getResumePage()));
    }

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
    public void updateQuestionContent(UpdateQuestionContentRequest request, long resumeId, long questionId, User user) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 질문 존재 여부 및 수정 권한 확인
        Question question = findById(questionId);
        question.validateUser(user);

        question.updateContent(request.getContent());
    }

    @Transactional
    public void updateQuestionCheck(UpdateQuestionCheckRequest request, long resumeId, long questionId, User user) {

        // 이력서 존재 여부 및 체크 수정 권한 확인
        Resume resume = resumeService.findById(resumeId);
        resume.validateUser(user);

        // 해당 이력서에 예상 질문 존재 여부 확인
        Question question = validateQuestionByResumeId(questionId, resumeId);

        question.updateChecked(request.isChecked());
    }

    @Transactional
    public void updateQuestionBookmark(UpdateQuestionBookmarkRequest request, long resumeId, long questionId, User user) {

        // 이력서 존재 여부 및 체크 수정 권한 확인
        Resume resume = resumeService.findById(resumeId);
        resume.validateUser(user);

        // 해당 이력서에 예상 질문 존재 여부 확인
        Question question = validateQuestionByResumeId(questionId, resumeId);

        question.updateBookmarked(request.isBookmarked());
    }

    @Transactional
    public List<Label> findQuestionLabels(long resumeId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        return labelRepository.findByResumeId(resumeId);
    }

    /**
     * labelId가 있다면 해당 labelId로 label을 찾고, 없다면 labelContent로 label을 생성
     * @param request
     * @param resume
     * @return
     */
    private Label verifyQuestionLabel(PostQuestionRequest request, Resume resume) {

        if (request.getLabelId() != null) return utilService.findById(request.getLabelId());
        if (request.getLabelContent() != null) return labelRepository.save(Label.ofCreated(resume, request.getLabelContent()));

        return null;
    }

    private Question findParentQuestion(long questionId, long resumeId, int resumePage) {

        Optional<Question> parentQuestion = questionRepository.findByIdAndResumeIdAndResumePageAndDeletedAtIsNull(questionId, resumeId, resumePage);

        if (parentQuestion.isEmpty()) throw new NonExistQuestionException("존재하지 않는 질문입니다.");

        // 예상 질문의 부모 질문이 존재하는 경우 예외
        if (parentQuestion.get().getParentQuestion() != null) throw new NonExistQuestionException("해당 예상 질문에는 댓글을 달 수 없습니다.");

        return parentQuestion.get();
    }

    private Question findById(long questionId) {

        return questionRepository.findByIdAndDeletedAtIsNull(questionId)
                .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 질문입니다."));
    }

    private Question validateQuestionByResumeId(long questionId, long resumeId) {

        return questionRepository.findByIdAndResumeIdAndDeletedAtIsNull(questionId, resumeId)
                .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 질문입니다."));
    }
}
