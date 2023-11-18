package reviewme.be.comment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "commend", description = "댓글(comment) API")
@RequestMapping("/resume/{resumeId}/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {
}
