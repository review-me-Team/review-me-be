package reviewme.be.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.friend.entity.Friend;
import reviewme.be.friend.exception.AlreadyFriendRelationException;
import reviewme.be.friend.exception.AlreadyFriendRequestedException;
import reviewme.be.friend.exception.NotOnTheFriendRelationException;
import reviewme.be.friend.exception.NotOnTheFriendRequestException;
import reviewme.be.friend.repository.FriendRepository;
import reviewme.be.user.service.UserService;
import reviewme.be.util.entity.User;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    @Transactional
    public void requestFriend(long userId, long followingUserId) {

        User followerUser = userService.getUserById(userId);
        User followingUser = userService.getUserById(followingUserId);

        if (friendRepository.isRequested(userId, followingUserId)) {
            throw new AlreadyFriendRequestedException("이미 친구 요청을 보냈습니다.");
        }

        if (friendRepository.isFriend(userId, followingUserId)) {
            throw new AlreadyFriendRelationException("이미 친구 관계인 회원입니다.");
        }

        friendRepository.save(
                Friend.newRequest(followerUser, followingUser));
    }

    @Transactional
    public void acceptFriend(long userId, long followingUserId) {

        User user = userService.getUserById(userId);
        User followingUser = userService.getUserById(followingUserId);

        if (friendRepository.isFriend(userId, followingUserId)) {
            throw new AlreadyFriendRelationException("이미 친구 관계인 회원입니다.");
        }

        if (!friendRepository.isRequested(userId, followingUserId)) {
            throw new NotOnTheFriendRequestException("친구 요청 목록에 없습니다.");
        }

        Friend friend = friendRepository.findByFollowerUserIdAndFollowingUserIdAndAcceptedIsFalse(userId, followingUserId);

        friend.acceptRequest();

        friendRepository.save(
                Friend.newRelation(followingUser, user));
    }

    @Transactional
    public void deleteFriend(long userId, long followingUserId) {

        User user = userService.getUserById(userId);
        User followingUser = userService.getUserById(followingUserId);

        if (!friendRepository.isFriend(userId, followingUserId)) {
            throw new NotOnTheFriendRelationException("친구 관계가 아닙니다.");
        }

        Friend friendRequested = friendRepository.findByFollowerUserIdAndFollowingUserIdAndAcceptedIsTrue(userId, followingUserId);
        Friend friendRelation = friendRepository.findByFollowerUserIdAndFollowingUserIdAndAcceptedIsTrue(followingUserId, userId);

        friendRepository.delete(friendRequested);
        friendRepository.delete(friendRelation);
    }

    public boolean isFriend(long userId, long friendId) {

        return friendRepository.isFriend(userId, friendId);
    }
}
