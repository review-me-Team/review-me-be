package reviewme.be.comment.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.entity.Emoji;
import reviewme.be.util.vo.EmojisVO;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentEmojiRepository commentEmojiRepository;
    private final ResumeService resumeService;
    private final EmojisVO emojisVO;

    @Transactional
    public void saveComment(User commenter, long resumeId, PostCommentRequest request) {

        Resume resume = resumeService.findById(resumeId);

        commentRepository.save(
            new Comment(commenter, resume, request.getContent())
        );
    }

    @Transactional
    public CommentPageResponse getComments(long resumeId, Pageable pageable, User user) {

        resumeService.findById(resumeId);

        // 댓글 목록 조회와 내가 선택한 이모지 조회
        Page<CommentResponse> commentPage = commentRepository.findCommentsByResumeId(resumeId,
            user.getId(), pageable);

        List<CommentResponse> comments = commentPage.getContent();

        comments.forEach(comment -> {
            List<EmojiCount> emojiCounts = commentEmojiRepository.findCommentEmojiCountByCommentId(
                comment.getId());
            comment.setEmojis(emojiCounts);
        });

        return CommentPageResponse.builder()
            .comments(comments)
            .pageNumber(commentPage.getNumber())
            .lastPage(commentPage.getTotalPages() - 1)
            .pageSize(commentPage.getSize())
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
    public void updateComment(UpdateCommentContentRequest request, User user, long resumeId,
        long commentId) {

        resumeService.findById(resumeId);

        Comment comment = findById(commentId);
        comment.validateUser(user);

        comment.updateContent(request.getContent(), LocalDateTime.now());
    }

    @Transactional
    public void updateCommentEmoji(UpdateCommentEmojiRequest request,
        long resumeId, long commentId, User user) {

        // 이력서로 댓글 존재 여부 검증
        Comment comment = findByIdAndResumeId(commentId, resumeId);

        // 기존 이모지 삭제
        Optional<CommentEmoji> commentEmoji = commentEmojiRepository.findByUserIdAndCommentId(
            user.getId(), commentId);

        Emoji emoji = emojisVO.findEmojiById(request.getId());

        if (commentEmoji.isPresent()) {
            commentEmoji.get().updateEmoji(emojisVO.findEmojiById(request.getId()));
            return;
        }

        commentEmojiRepository.save(
            CommentEmoji.ofCreated(user, comment, emoji)
        );
    }

    private Comment findById(long commentId) {

        return commentRepository.findByIdAndDeletedAtIsNull(commentId)
            .orElseThrow(() -> new NonExistCommentException("존재하지 않는 댓글입니다."));
    }

    private Comment findByIdAndResumeId(long commentId, long resumeId) {

        return commentRepository.findByIdAndResumeIdAndDeletedAtIsNull(commentId, resumeId)
            .orElseThrow(() -> new NonExistCommentException("존재하지 않는 댓글입니다."));
    }
}
