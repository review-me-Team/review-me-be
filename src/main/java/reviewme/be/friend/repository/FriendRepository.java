package reviewme.be.friend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reviewme.be.friend.entity.Friend;
import reviewme.be.user.entity.User;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {

    @Query(value = "SELECT CASE WHEN COUNT(friend) > 0 THEN true ELSE false END " +
            "FROM Friend friend " +
            "WHERE friend.followerUser.id = :userId " +
            "AND friend.followingUser.id = :followingUserId " +
            "AND friend.accepted = true")
    boolean isFriend(@Param("userId") long userId, @Param("followingUserId") long followingUserId);

    Optional<Friend> findByFollowerUserAndFollowingUserAndAcceptedIsFalse(User followerUser, User followingUser);
}
