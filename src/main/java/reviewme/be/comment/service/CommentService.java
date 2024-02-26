package reviewme.be.comment.service;

import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.comment.dto.CommentInfo;
import reviewme.be.comment.dto.request.PostCommentRequest;
import reviewme.be.comment.dto.request.UpdateCommentContentRequest;
import reviewme.be.comment.dto.request.UpdateCommentEmojiRequest;
import reviewme.be.comment.dto.response.CommentPageResponse;
import reviewme.be.comment.dto.response.CommentResponse;
import reviewme.be.comment.entity.Comment;
import reviewme.be.comment.entity.CommentEmoji;
import reviewme.be.comment.exception.NonExistCommentException;
import reviewme.be.comment.repository.CommentEmojiRepository;
import reviewme.be.comment.repository.CommentRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.user.service.UserService;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.entity.Emoji;
import reviewme.be.util.service.UtilService;
import reviewme.be.util.vo.EmojisVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentEmojiRepository commentEmojiRepository;
    private final UserService userService;
    private final ResumeService resumeService;
    private final UtilService utilService;
    private final EmojisVO emojisVO;

    @Transactional
    public void saveComment(User commenter, long resumeId, PostCommentRequest postComment) {

        Resume resume = resumeService.findById(resumeId);
        LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Comment savedComment = commentRepository.save(
            new Comment(commenter, resume, postComment.getContent(), createdAt)
        );

        // Default Comment Emojis 생성
        commentEmojiRepository.saveAll(
            CommentEmoji.createDefaultCommentEmojis(
                savedComment,
                emojisVO.getEmojis())
        );
    }

    @Transactional
    public CommentPageResponse getComments(long resumeId, Pageable pageable, User user) {

        resumeService.findById(resumeId);

        // 댓글 목록 조회 후 id 목록 추출
        Page<CommentInfo> comments = commentRepository.findCommentsByResumeId(resumeId, pageable);
        List<Long> commentIds = getCommentIds(comments);

        // 댓글별 이모지 개수 조회
        List<List<EmojiCount>> emojiCounts = utilService.collectEmojiCounts(
            commentEmojiRepository.findEmojiCountByCommentIds(commentIds));

        // 내가 선택한 이모지 목록 조회
        List<Integer> myEmojiIds = utilService.getMyEmojiIds(commentEmojiRepository.findByUserIdAndCommentIdIn(
            user.getId(), commentIds));

        List<CommentResponse> commentsResponse = collectToCommentsResponse(commentIds, comments,
            emojiCounts, myEmojiIds);

        return CommentPageResponse.builder()
            .comments(commentsResponse)
            .pageNumber(comments.getNumber())
            .lastPage(comments.getTotalPages() - 1)
            .pageSize(comments.getSize())
            .build();
    }

    @Transactional
    public void deleteComment(User user, long resumeId, long commentId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 댓글 존재 여부 및 삭제 권한 확인
        Comment comment = findById(commentId);
        comment.validateUser(user);

        // 댓글 삭제, 댓글에 달린 이모지 삭제
        comment.softDelete(LocalDateTime.now());
        commentEmojiRepository.deleteAllByCommentId(commentId);
    }

    @Transactional
    public void updateComment(UpdateCommentContentRequest updateComment, User user, long resumeId,
        long commentId) {

        resumeService.findById(resumeId);

        Comment comment = findById(commentId);
        comment.validateUser(user);

        comment.updateContent(updateComment.getContent(), LocalDateTime.now());
    }

    @Transactional
    public void updateCommentEmoji(UpdateCommentEmojiRequest updateCommentEmoji,
        long commentId, User user) {

        Comment comment = findById(commentId);

        Integer emojiId = updateCommentEmoji.getId();

        Optional<CommentEmoji> myCommentEmoji = commentEmojiRepository.findByUserIdAndCommentId(
            user.getId(), commentId);

        Emoji emoji = emojisVO.findEmojiById(emojiId);

        if (myCommentEmoji.isEmpty()) {
            commentEmojiRepository.save(CommentEmoji.ofCreated(
                userService.getUserById(user.getId()),
                comment,
                emoji));
            return;
        }

        if (emojiId != null && !emojisVO.validateEmojiById(emojiId)) {
            throw new NonExistCommentException("존재하지 않는 이모지입니다.");
        }

        myCommentEmoji.get().updateEmoji(emoji);
    }

    private Comment findById(long commentId) {

        return commentRepository.findByIdAndDeletedAtIsNull(commentId)
            .orElseThrow(() -> new NonExistCommentException("존재하지 않는 댓글입니다."));
    }

    /***************
     * 아래는 댓글 목록 조회 시 사용되는 메서드입니다.
     ***************/
    private List<Long> getCommentIds(Page<CommentInfo> comments) {
        return comments.stream()
            .map(CommentInfo::getId)
            .collect(Collectors.toList());
    }

    /**
     * 댓글 목록을 응답 형태로 변환
     */
    private List<CommentResponse> collectToCommentsResponse(List<Long> commentIds,
        Page<CommentInfo> comments,
        List<List<EmojiCount>> emojiCounts, List<Integer> myEmojiIds) {

        List<CommentResponse> commentsResponse = new ArrayList<>();
        for (int commentIdx = 0; commentIdx < commentIds.size(); commentIdx++) {

            CommentInfo comment = comments.getContent().get(commentIdx);
            List<EmojiCount> emojis = emojiCounts.get(commentIdx);
            Integer myEmojiId = myEmojiIds.get(commentIdx);

            commentsResponse.add(
                CommentResponse.fromComment(
                    comment,
                    emojis,
                    myEmojiId));
        }

        return commentsResponse;
    }
}
