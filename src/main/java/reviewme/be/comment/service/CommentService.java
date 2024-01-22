package reviewme.be.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.comment.dto.request.PostCommentRequest;
import reviewme.be.comment.entity.Comment;
import reviewme.be.comment.repository.CommentRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.user.service.UserService;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
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
}
