package reviewme.be.comment.service;

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
import reviewme.be.util.exception.NotYoursException;
import reviewme.be.util.vo.EmojisVO;

import javax.persistence.Tuple;
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
    private final EmojisVO emojisVO;

    @Transactional
    public void saveComment(User commenter, long resumeId, PostCommentRequest postComment) {

        Resume resume = resumeService.findById(resumeId);

        commentRepository.save(
                Comment.ofCreated(commenter, resume, postComment.getContent())
        );
    }

    @Transactional
    public CommentPageResponse getComments(long userId, long resumeId, Pageable pageable) {

        resumeService.findById(resumeId);

        List<CommentResponse> commentResponses = new ArrayList<CommentResponse>();

        Page<CommentInfo> commentInfos = commentRepository.findByResumeId(userId, resumeId, pageable);

        for (CommentInfo comment : commentInfos) {
            List<EmojiCount> emojis = commentEmojiRepository.countByCommentIdGroupByEmojiId(comment.getId())
                    .stream()
                    .map(CommentService::tutpleToEmoji)
                    .collect(Collectors.toList());

            Optional<CommentEmoji> myCommentEmoji = commentEmojiRepository.findByUserIdAndCommentId(userId, comment.getId());

            Integer myEmojiId = myCommentEmoji.map(
                        commentEmoji -> commentEmoji.getEmoji().getId()
                    ).orElse(null);

            commentResponses.add(
                    CommentResponse.fromComment(
                            comment,
                            emojis,
                            myEmojiId));
        }

        return CommentPageResponse.builder()
                .comments(commentResponses)
                .pageNumber(commentInfos.getNumber())
                .lastPage(commentInfos.getTotalPages() - 1)
                .pageSize(commentInfos.getSize())
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
    public void updateComment(UpdateCommentContentRequest updateComment, User user, long resumeId, long commentId) {

        resumeService.findById(resumeId);

        Comment comment = findById(commentId);
        comment.validateUser(user);

        comment.updateContent(updateComment.getContent(), LocalDateTime.now());
    }

    @Transactional
    public void updateCommentEmoji(long userId, long commentId, UpdateCommentEmojiRequest updateCommentEmoji) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NonExistCommentException("존재하지 않는 댓글입니다."));

        Integer emojiId = updateCommentEmoji.getId();

        Optional<CommentEmoji> myCommentEmoji = commentEmojiRepository.findByUserIdAndCommentId(userId, commentId);

        Emoji emoji = emojisVO.findEmojiById(emojiId);

        if (myCommentEmoji.isEmpty()) {
            commentEmojiRepository.save(CommentEmoji.ofCreated(
                    userService.getUserById(userId),
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

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NonExistCommentException("[ERROR] 존재하지 않는 댓글입니다."));
    }

    private static EmojiCount tutpleToEmoji(Tuple tuple) {

        return EmojiCount.fromCountEmojiTuple(
                tuple.get("id", Integer.class),
                tuple.get("count", Long.class)
        );
    }
}
