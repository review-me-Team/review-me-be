package reviewme.be.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reviewme.be.friend.entity.Friend;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend save(Friend createdFriendRequest);

    Friend findByFollowerUserIdAndFollowingUserIdAndAcceptedIsFalse(long followerUserId, long followingUserId);

    Friend findByFollowerUserIdAndFollowingUserIdAndAcceptedIsTrue(long followerUserId, long followingUserId);

    List<Friend> findByFollowingUserIdAndAcceptedIsTrue(long followingUserId);

    long countByFollowingUserIdAndAcceptedIsTrue(long followingUserId);

    List<Friend> findByFollowingUserIdAndAcceptedIsFalse(long followingUserId);

    long countByFollowingUserIdAndAcceptedIsFalse(long followingUserId);

    @Query(value = "SELECT CASE WHEN COUNT(friend) > 0 THEN true ELSE false END " +
            "FROM Friend friend " +
            "WHERE friend.followerUser.id = :userId " +
            "AND friend.followingUser.id = :followingUserId " +
            "AND friend.accepted = true")
    boolean isFriend(@Param("userId") long userId, @Param("followingUserId") long followingUserId);

    @Query(value = "SELECT CASE WHEN COUNT(friend) > 0 THEN true ELSE false END " +
            "FROM Friend friend " +
            "WHERE friend.followerUser.id = :userId " +
            "AND friend.followingUser.id = :followingUserId " +
            "AND friend.accepted = false")
    boolean isRequested(@Param("userId") long userId, @Param("followingUserId")long followingUserId);
}
