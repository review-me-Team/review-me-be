package reviewme.be.question.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.question.dto.QuestionsFilter;
import reviewme.be.question.dto.request.*;
import reviewme.be.question.dto.response.QuestionCommentPageResponse;
import reviewme.be.question.dto.response.QuestionCommentResponse;
import reviewme.be.question.dto.response.QuestionPageResponse;
import reviewme.be.question.dto.response.QuestionResponse;
import reviewme.be.question.entity.Question;
import reviewme.be.question.entity.QuestionEmoji;
import reviewme.be.question.exception.NonExistQuestionException;
import reviewme.be.question.exception.NotParentQuestionException;
import reviewme.be.question.repository.QuestionEmojiRepository;
import reviewme.be.question.repository.QuestionRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.entity.Emoji;

import java.util.List;
import reviewme.be.util.vo.EmojisVO;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionEmojiRepository questionEmojiRepository;
    private final ResumeService resumeService;
    private final EmojisVO emojisVO;

    @Transactional
    public void saveQuestion(CreateQuestionRequest request, long resumeId, User commenter) {

        // 이력서 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);

        questionRepository.save(
            Question.createQuestion(commenter, resume, request.getLabelContent(),
                request.getContent(),
                request.getResumePage()));
    }

    @Transactional
    public void saveQuestionComment(CreateQuestionCommentRequest request, long resumeId,
        User commenter, long parentId) {

        // 이력서, 예상 질문 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        Question parentQuestion = findParentQuestionByIdAndResumeId(parentId, resumeId);

        if (!parentQuestion.isParentQuestion()) {
            throw new NotParentQuestionException("해당 예상 질문에는 대댓글을 추가할 수 없습니다.");
        }

        questionRepository.save(Question.createQuestionComment(
            commenter,
            resume,
            parentQuestion,
            request.getContent()));

        parentQuestion.plusChildCnt();
    }

    @Transactional(readOnly = true)
    public QuestionPageResponse getQuestions(long resumeId, QuestionsFilter questionsFilter,
        User user,
        Pageable pageable) {

        // 이력서 존재 여부 확인 및 접근 권한 확인
        Resume resume = resumeService.findById(resumeId);
        resumeService.validateAccessRights(resume, user);
        boolean isWriter = resume.isWriter(user);

        // 예상 질문 목록 조회
        Page<QuestionResponse> questionPage = questionRepository.findQuestionsByResumeIdAndResumePage(
            resumeId, user.getId(), questionsFilter, pageable);

        List<QuestionResponse> questions = questionPage.getContent();

        // 예상 질문에 대한 이모지 목록 조회
        questions.forEach(question -> {
            List<EmojiCount> emojiCounts = questionEmojiRepository.findQuestionEmojiCountByQuestionId(
                question.getId());
            question.setEmojis(emojiCounts);

            if (!isWriter) {
                question.setBookmarked(null);
            }
        });

        return QuestionPageResponse.builder()
            .questions(questions)
            .pageNumber(questionPage.getNumber())
            .lastPage(questionPage.getTotalPages() - 1)
            .pageSize(questionPage.getSize())
            .build();
    }

    @Transactional(readOnly = true)
    public QuestionCommentPageResponse getQuestionComments(long resumeId, long parentQuestionId,
        User user,
        Pageable pageable) {

        // 이력서, 부모 예상 질문 존재 여부 확인 및 접근 권한 검증
        Resume resume = resumeService.findById(resumeId);
        resumeService.validateAccessRights(resume, user);
        findParentQuestionById(parentQuestionId);

        // 예상 질문에 달린 대댓글 목록 조회
        Page<QuestionCommentResponse> questionCommentPage = questionRepository.findQuestionCommentsByQuestionId(
            parentQuestionId, user.getId(), pageable);

        List<QuestionCommentResponse> questionComments = questionCommentPage.getContent();

        questionComments.forEach(questionComment -> {
            List<EmojiCount> emojiCounts = questionEmojiRepository.findQuestionEmojiCountByQuestionId(
                questionComment.getId());
            questionComment.setEmojis(emojiCounts);
        });

        questionComments = sortQuestionCommentsByIdAsc(questionComments);

        return QuestionCommentPageResponse.builder()
            .questionComments(questionComments)
            .pageNumber(questionCommentPage.getNumber())
            .lastPage(questionCommentPage.getTotalPages() - 1)
            .pageSize(questionCommentPage.getSize())
            .build();
    }

    @Transactional
    public void deleteQuestion(long resumeId, long questionId, User user) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 질문 존재 여부 및 삭제 권한 확인
        Question question = findById(questionId);
        question.validateUser(user);
        LocalDateTime deletedAt = LocalDateTime.now();
        question.softDelete(deletedAt);
        questionEmojiRepository.deleteAllByQuestionIdAndUserIdIsNotNull(questionId);

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

        question.updateContent(request.getLabelContent(), request.getContent());
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
    public void updateQuestionEmoji(UpdateQuestionEmojiRequest request, long resumeId,
        long questionId, User user) {

        // 이력서로 피드백 존재 여부 확인
        Question question = findByIdAndResumeId(questionId, resumeId);

        // 기존 이모지 삭제
        Optional<QuestionEmoji> questionEmoji = questionEmojiRepository.findByQuestionIdAndUserId(
            questionId, user.getId());

        Emoji emoji = emojisVO.findEmojiById(request.getId());

        if (questionEmoji.isPresent()) {
            questionEmoji.get().updateEmoji(emojisVO.findEmojiById(request.getId()));
            return;
        }

        questionEmojiRepository.save(
            new QuestionEmoji(user, question, emoji)
        );
    }

    private Question findById(long questionId) {

        return questionRepository.findByIdAndDeletedAtIsNull(questionId)
            .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 질문입니다."));
    }

    private Question findByIdAndResumeId(long questionId, long resumeId) {

        return questionRepository.findByIdAndResumeIdAndDeletedAtIsNull(questionId, resumeId)
            .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 질문입니다."));
    }

    // 대댓글 목록 조회 시 해당 예상 질문이 조회되는 조건을 가지는 예상 질문인지 검증
    private Question findParentQuestionById(long questionId) {

        return questionRepository.findQuestionById(questionId)
            .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 예상 질문입니다."));
    }

    // 대댓글 추가 시 삭제된 예상 질문에도 추가할 수 있다.
    private Question findParentQuestionByIdAndResumeId(long questionId, long resumeId) {

        return questionRepository.findParentQuestionByIdAndResumeId(questionId, resumeId)
            .orElseThrow(() -> new NonExistQuestionException("존재하지 않는 예상 질문입니다."));
    }

    /**
     * 대댓글 조회 시 id 오름차순으로 재정렬
     */
    private List<QuestionCommentResponse> sortQuestionCommentsByIdAsc(
        List<QuestionCommentResponse> questionComments) {

        return questionComments.stream()
            .sorted(Comparator.comparingLong(QuestionCommentResponse::getId))
            .collect(Collectors.toList());
    }
}
