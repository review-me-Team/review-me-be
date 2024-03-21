package reviewme.be.question.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.question.dto.QuestionCommentInfo;
import reviewme.be.question.dto.QuestionInfo;
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
    private final UtilService utilService;
    private final EmojisVO emojisVO;

    @Transactional
    public void saveQuestion(CreateQuestionRequest request, long resumeId, User commenter) {

        // 이력서 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);

        Question savedQuestion = questionRepository.save(
            Question.createQuestion(commenter, resume, request.getLabelContent(), request.getContent(),
                request.getResumePage()));

        saveDefaultEmojis(savedQuestion);
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

        Question savedQuestion = questionRepository.save(Question.createQuestionComment(
            commenter,
            resume,
            parentQuestion,
            request.getContent()));

        parentQuestion.plusChildCnt();

        saveDefaultEmojis(savedQuestion);
    }

    @Transactional(readOnly = true)
    public QuestionPageResponse getQuestions(long resumeId, int resumePage,
        User user,
        Pageable pageable) {

        // 이력서 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        boolean isWriter = resume.isWriter(user);

        // 예상 질문 목록 조회 후 id 목록 추출
        Page<QuestionInfo> questionPage = questionRepository.findQuestionsByResumeIdAndResumePage(
            resumeId, user.getId(), resumePage, pageable);
        List<QuestionInfo> questions = questionPage.getContent();
        List<Long> questionIds = extractQuestionIds(questions);

        List<List<EmojiCount>> emojiCounts = utilService.collectEmojiCounts(
            questionEmojiRepository.findEmojiCountByQuestionIds(questionIds));

        List<QuestionResponse> questionsResponse = collectToQuestionsResponse(questionIds,
            questions, emojiCounts, isWriter);

        return QuestionPageResponse.builder()
            .questions(questionsResponse)
            .pageNumber(questionPage.getNumber())
            .lastPage(questionPage.getTotalPages() - 1)
            .pageSize(questionPage.getSize())
            .build();
    }

    @Transactional(readOnly = true)
    public QuestionCommentPageResponse getQuestionComments(long resumeId, long parentQuestionId,
        User user,
        Pageable pageable) {

        // 이력서, 부모 예상 질문 존재 여부 확인
        resumeService.findById(resumeId);
        findParentQuestionById(parentQuestionId);

        // 예상 질문에 달린 대댓글 목록 조회
        Page<QuestionCommentInfo> questionCommentPage = questionRepository.findQuestionCommentsByQuestionId(
            parentQuestionId, user.getId(), pageable);
        List<QuestionCommentInfo> questionComments = questionCommentPage.getContent();
        questionComments = sortQuestionCommentsByIdAsc(questionComments);

        List<Long> questionCommentIds = extractQuestionCommentIds(questionComments);

        List<List<EmojiCount>> emojiCounts = utilService.collectEmojiCounts(
            questionEmojiRepository.findEmojiCountByQuestionIds(questionCommentIds));

        List<QuestionCommentResponse> questionCommentsResponse = collectToQuestionCommentsResponse(
            questionCommentIds, questionComments, emojiCounts);

        return QuestionCommentPageResponse.builder()
            .questionComments(questionCommentsResponse)
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

    private void saveDefaultEmojis(Question savedQuestion) {

        questionEmojiRepository.saveAll(
            QuestionEmoji.createDefaultQuestionEmojis(
                savedQuestion,
                emojisVO.getEmojis())
        );
    }

    /***************
     * 아래는 예상 질문(또는 대댓글) 목록 조회 시 사용되는 메서드입니다.
     ***************/
    private List<Long> extractQuestionIds(List<QuestionInfo> questions) {

        return questions.stream()
            .map(QuestionInfo::getId)
            .collect(Collectors.toList());
    }

    private List<QuestionResponse> collectToQuestionsResponse(List<Long> questionIds,
        List<QuestionInfo> questions,
        List<List<EmojiCount>> emojiCounts, boolean isWriter) {

        List<QuestionResponse> questionsResponse = new ArrayList<>();

        for (int questionIdx = 0; questionIdx < questionIds.size(); questionIdx++) {

            QuestionInfo question = questions.get(questionIdx);
            List<EmojiCount> emojiCount = emojiCounts.get(questionIdx);

            QuestionResponse questionResponse = isWriter
                ? QuestionResponse.fromQuestionOfOwnResume(question, emojiCount)
                : QuestionResponse.fromQuestionOfOtherResume(question, emojiCount);

            questionsResponse.add(questionResponse);
        }

        return questionsResponse;
    }

    private List<Long> extractQuestionCommentIds(List<QuestionCommentInfo> questionComments) {

        return questionComments.stream()
            .map(QuestionCommentInfo::getId)
            .collect(Collectors.toList());
    }

    private List<QuestionCommentResponse> collectToQuestionCommentsResponse(
        List<Long> questionCommentIds,
        List<QuestionCommentInfo> questionComments,
        List<List<EmojiCount>> emojiCounts) {

        List<QuestionCommentResponse> questionCommentResponses = new ArrayList<>();

        for (int questionCommentIdx = 0; questionCommentIdx < questionCommentIds.size();
            questionCommentIdx++) {

            QuestionCommentInfo questionComment = questionComments.get(questionCommentIdx);
            List<EmojiCount> emojiCount = emojiCounts.get(questionCommentIdx);

            questionCommentResponses.add(
                QuestionCommentResponse.fromQuestionComment(questionComment, emojiCount)
            );
        }

        return questionCommentResponses;
    }

    /**
     * 대댓글 조회 시 id 오름차순으로 재정렬
     */
    private List<QuestionCommentInfo> sortQuestionCommentsByIdAsc(List<QuestionCommentInfo> questionCommentInfos) {

        return questionCommentInfos.stream()
            .sorted(Comparator.comparingLong(QuestionCommentInfo::getId))
            .collect(Collectors.toList());
    }
}
