package reviewme.be.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reviewme.be.friend.entity.Friend;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend save(Friend createdFriendRequest);

    List<Friend> findByFollowingUserIdAndAcceptedIsTrue(long followingUserId);

    long countByFollowingUserIdAndAcceptedIsTrue(long followingUserId);

    List<Friend> findByFollowingUserIdAndAcceptedIsFalse(long followingUserId);

    long countByFollowingUserIdAndAcceptedIsFalse(long followingUserId);

    @Query(value = "SELECT CASE WHEN COUNT(friend) > 0 THEN true ELSE false END " +
            "FROM Friend friend " +
            "WHERE friend.followerUser.id = :userId " +
            "AND friend.followingUser.id = :friendId " +
            "AND friend.accepted = true")
    boolean isFriend(long userId, long friendId);

    @Query(value = "SELECT CASE WHEN COUNT(friend) > 0 THEN true ELSE false END " +
            "FROM Friend friend " +
            "WHERE friend.followerUser.id = :userId " +
            "AND friend.followingUser.id = :friendId " +
            "AND friend.accepted = false")
    boolean isRequested(long userId, long friendId);
}
