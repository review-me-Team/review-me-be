package reviewme.be.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.comment.dto.request.PostCommentRequest;
import reviewme.be.comment.dto.request.UpdateCommentContentRequest;
import reviewme.be.comment.entity.Comment;
import reviewme.be.comment.exception.NonExistCommentException;
import reviewme.be.comment.repository.CommentEmojiRepository;
import reviewme.be.comment.repository.CommentRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.user.service.UserService;
import reviewme.be.util.exception.NotYoursException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentEmojiRepository commentEmojiRepository;
    private final UserService userService;
    private final ResumeService resumeService;

    @Transactional
    public void saveComment(long commenterId, long resumeId, PostCommentRequest postComment) {

        User commenter = userService.getUserById(commenterId);
        Resume resume = resumeService.getResumeById(resumeId);

        commentRepository.save(
                Comment.ofCreated(commenter, resume, postComment.getContent())
        );
    }

    @Transactional
    public void deleteComment(long userId, long commentId) {

        Comment comment = validateCommentModifyAuthority(userId, commentId);

        comment.softDelete(LocalDateTime.now());
        commentEmojiRepository.deleteAllByCommentId(commentId);
    }

    @Transactional
    public void updateComment(long userId, long commentId, UpdateCommentContentRequest updateComment) {

        Comment comment = validateCommentModifyAuthority(userId, commentId);

        comment.updateContent(updateComment.getContent(), LocalDateTime.now());
    }

    private Comment validateCommentModifyAuthority(long userId, long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NonExistCommentException("[ERROR] 존재하지 않는 댓글입니다."));

        if (comment.getCommenter().getId() != userId) {
            throw new NotYoursException("본인이 작성한 댓글이 아닙니다.");
        }

        return comment;
    }
}
