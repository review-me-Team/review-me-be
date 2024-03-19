package reviewme.be.friend.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.friend.entity.Friend;
import reviewme.be.user.dto.response.UserResponse;

public interface FriendRepositoryCustom {

    Page<UserResponse> findFriendsByUserId(long userId, String start, boolean accepted, Pageable pageable);

    Page<UserResponse> findSentFriendRequests(long followerId, String start, Pageable pageable);

    List<Friend> findFriendRelation(long followerUserId, long followingUserId);
}
