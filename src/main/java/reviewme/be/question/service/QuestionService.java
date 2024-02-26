package reviewme.be.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.question.dto.request.*;
import reviewme.be.question.entity.Question;
import reviewme.be.question.entity.QuestionEmoji;
import reviewme.be.question.exception.NonExistQuestionException;
import reviewme.be.question.exception.NotParentQuestionException;
import reviewme.be.question.repository.QuestionEmojiRepository;
import reviewme.be.question.repository.QuestionRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.util.entity.Label;
import reviewme.be.util.repository.LabelRepository;
import reviewme.be.util.service.UtilService;

import java.util.List;
import reviewme.be.util.vo.EmojisVO;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionEmojiRepository questionEmojiRepository;
    private final ResumeService resumeService;
    private final LabelRepository labelRepository;
    private final UtilService utilService;
    private final EmojisVO emojisVO;

    @Transactional
    public void saveQuestion(CreateQuestionRequest request, long resumeId, User commenter) {

        // 이력서 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);

        // 예상 질문 라벨 조회 (없다면 생성)
        Label label = verifyQuestionLabel(request, resume);

        Question savedQuestion = questionRepository.save(
            Question.createQuestion(commenter, resume, label, request.getContent(),
                request.getResumePage()));

        saveDefaultEmojis(savedQuestion);
    }

    @Transactional
    public void saveQuestionComment(CreateQuestionCommentRequest request, long resumeId,
        User commenter, long parentId) {

        // 이력서, 예상 질문 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        Question parentQuestion = findByIdAndResumeId(parentId, resumeId);

        if (!parentQuestion.isParentQuestion()) {
            throw new NotParentQuestionException("해당 예상 질문에는 대댓글을 추가할 수 없습니다.");
        }

        Question savedQuestion = questionRepository.save(Question.createQuestionComment(
            commenter,
            resume,
            parentQuestion,
            request.getContent()));

        parentQuestion.plusChildCnt();

        saveDefaultEmojis(savedQuestion);
    }

    @Transactional
    public void deleteQuestion(long resumeId, long questionId, User user) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 질문 존재 여부 및 삭제 권한 확인
        Question question = findById(questionId);
        question.validateUser(user);

        question.softDelete();

        if (question.getParentQuestion() != null) {
            question.getParentQuestion().minusChildCnt();
        }
    }

    @Transactional
    public void updateQuestionContent(UpdateQuestionContentRequest request, long resumeId,
        long questionId, User user) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 질문 존재 여부 및 수정 권한 확인
        Question question = findById(questionId);
        question.validateUser(user);

        question.updateContent(request.getContent());
    }

    @Transactional
    public void updateQuestionCheck(UpdateQuestionCheckRequest request, long resumeId,
        long questionId, User user) {

        // 이력서 존재 여부 및 체크 수정 권한 확인
        Resume resume = resumeService.findById(resumeId);
        resume.validateUser(user);

        // 해당 이력서에 예상 질문 존재 여부 확인
        Question question = findByIdAndResumeId(questionId, resumeId);

        question.updateChecked(request.isChecked());
    }

    @Transactional
    public void updateQuestionBookmark(UpdateQuestionBookmarkRequest request, long resumeId,
        long questionId, User user) {

        // 이력서 존재 여부 및 체크 수정 권한 확인
        Resume resume = resumeService.findById(resumeId);
        resume.validateUser(user);

        // 해당 이력서에 예상 질문 존재 여부 확인
        Question question = findByIdAndResumeId(questionId, resumeId);

        question.updateBookmarked(request.isBookmarked());
    }

    @Transactional
    public List<Label> findQuestionLabels(long resumeId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        return labelRepository.findByResumeIdOrderByContentAsc(resumeId);
    }

    /**
     * labelId가 있다면 해당 labelId로 label을 찾고, 이미 존재하는 labelContent라면 해당 label을 반환하고, 없다면 새로 생성
     *
     * @param request (Optional labelId, Optional labelContent)
     * @param resume
     * @return
     */
    private Label verifyQuestionLabel(CreateQuestionRequest request, Resume resume) {

        if (request.getLabelId() != null) {

            return utilService.findById(request.getLabelId());
        }

        if (request.getLabelContent() != null && !request.getLabelContent().isEmpty()) {

            return labelRepository.findByResumeIdAndContent(resume.getId(),
                    request.getLabelContent())
                .orElseGet(
                    () -> labelRepository.save(Label.ofCreated(resume, request.getLabelContent())));
        }

        return null;
    }

    private Question findById(long questionId) {

        return questionRepository.findByIdAndDeletedAtIsNull(questionId)
            .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 질문입니다."));
    }

    private Question findByIdAndResumeId(long questionId, long resumeId) {

        return questionRepository.findByIdAndResumeIdAndDeletedAtIsNull(questionId, resumeId)
            .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 질문입니다."));
    }

    private void saveDefaultEmojis(Question savedQuestion) {

        questionEmojiRepository.saveAll(
            QuestionEmoji.createDefaultQuestionEmojis(
                savedQuestion,
                emojisVO.getEmojis())
        );
    }
}
