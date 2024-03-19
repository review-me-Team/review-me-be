package reviewme.be.friend.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.friend.entity.Friend;
import reviewme.be.friend.exception.AlreadyFriendRelationException;
import reviewme.be.friend.exception.AlreadyFriendRequestedException;
import reviewme.be.friend.exception.NotOnTheFriendRelationException;
import reviewme.be.friend.exception.NotOnTheFriendRequestException;
import reviewme.be.friend.repository.FriendRepository;
import reviewme.be.friend.request.AcceptFriendRequest;
import reviewme.be.friend.request.FollowFriendRequest;
import reviewme.be.user.dto.response.UserResponse;
import reviewme.be.user.service.UserService;
import reviewme.be.user.entity.User;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    @Transactional
    public void requestFriend(FollowFriendRequest request, User followerUser) {

        // TODO: 상대방이 이미 나에게 친구 요청 보낸 사람인 경우 플로우 회의 후 로직 추가

        // 친구 요청할 사용자 확인
        User followingUser = userService.getUserById(request.getUserId());

        // 이미 친구 요청한 상태이거나 이미 친구인 상태라면 예외
        validateNewFriendRequest(followerUser, followingUser);
        validateIsAlreadyFriend(followerUser, followingUser);

        friendRepository.save(
            Friend.newRequest(followerUser, followingUser));
    }

    @Transactional
    public void acceptFriend(AcceptFriendRequest request, User followingUser) {

        // 나에게 친구 요청을 보낸 사람
        User followerUser = userService.getUserById(request.getUserId());

        // 이미 친구인 상태거나 친구 요청 목록에 없다면 예외
        validateIsAlreadyFriend(followingUser, followerUser);
        Friend friendRequest = getFriendRequest(followerUser, followingUser);

        // 친구 요청 수락 및 새로운 관계 형성
        friendRequest.accept();
        friendRepository.save(
            Friend.newRelation(followingUser, followerUser));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getFriends(User user, String start, Pageable pageable) {

        userService.validateLoggedInUser(user);

        boolean isFriend = true;

        return friendRepository.findFriendsByUserId(user.getId(), start, isFriend, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getReceivedFriendRequests(User user, String start, Pageable pageable) {

        userService.validateLoggedInUser(user);

        boolean isFriend = false;

        return friendRepository.findFriendsByUserId(user.getId(), start, isFriend, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getSentFriendRequests(User user, String start, Pageable pageable) {

        userService.validateLoggedInUser(user);

        return friendRepository.findSentFriendRequests(user.getId(), start, pageable);
    }

    @Transactional
    public void deleteFriend(long friendId, User user) {

        // 존재하는 유저인지 확인
        User friend = userService.getUserById(friendId);

        // 친구 관계가 아니라면 예외
        validateIsFriend(user, friend);

        // 친구 관계 삭제
        List<Friend> friendRelation = friendRepository.findFriendRelation(user.getId(), friend.getId());
        friendRepository.deleteAll(friendRelation);
    }

    @Transactional
    public void rejectFriendRequest(long followerUserId, User user) {

        // 친구 요청을 보낸 사람 검증
        User followerUser = userService.getUserById(followerUserId);

        // 친구 요청 목록에 없다면 예외
        Friend friendRequest = getFriendRequest(followerUser, user);
        friendRepository.delete(friendRequest);
    }

    @Transactional
    public void cancelFriendRequest(long followingUserId, User user) {

        // 친구 요청을 보낸 사람 검증
        User followingUser = userService.getUserById(followingUserId);

        // 친구 요청 목록에 없다면 예외
        Friend friendRequest = getFriendRequest(user, followingUser);
        friendRepository.delete(friendRequest);
    }

    public boolean isFriend(long userId, long friendId) {

        return friendRepository.isFriend(userId, friendId);
    }

    private void validateNewFriendRequest(User followerUser, User followingUser) {

        friendRepository.findByFollowerUserAndFollowingUserAndAcceptedIsFalse(followerUser, followingUser)
            .ifPresent(friend -> {
                throw new AlreadyFriendRequestedException("이미 친구 요청을 보냈습니다.");
            });
    }

    private void validateIsAlreadyFriend(User followerUser, User followingUser) {

        if (friendRepository.isFriend(followerUser.getId(), followingUser.getId())) {
            throw new AlreadyFriendRelationException("이미 친구 관계인 회원입니다.");
        }
    }

    private Friend getFriendRequest(User followerUser, User followingUser) {

        return friendRepository.findByFollowerUserAndFollowingUserAndAcceptedIsFalse(followerUser, followingUser)
            .orElseThrow(() -> new NotOnTheFriendRequestException("친구 요청 목록에 없습니다."));
    }

    private void validateIsFriend(User user, User friend) {

        if (!friendRepository.isFriend(user.getId(), friend.getId())) {
            throw new NotOnTheFriendRelationException("친구 관계가 아닙니다.");
        }
    }
}
