package reviewme.be.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.friend.entity.Friend;
import reviewme.be.friend.repository.FriendRepository;
import reviewme.be.user.service.UserService;
import reviewme.be.util.entity.User;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    @Transactional
    public void requestFriend(long userId, long friendId) {

        User followerUser = userService.getUserById(userId);
        User followingUser = userService.getUserById(friendId);

        friendRepository.save(
                Friend.ofCreated(followerUser, followingUser));
    }

    public boolean isFriend(long userId, long friendId) {

        return friendRepository.isFriend(userId, friendId) || friendRepository.isFriend(friendId, userId);
    }
}
