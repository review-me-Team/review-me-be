package reviewme.be.friend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "friend", description = "친구(friend) API")
@RequestMapping("/friend")
@RestController
@RequiredArgsConstructor
public class FriendController {
}
