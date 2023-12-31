package reviewme.be.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.friend.entity.Friend;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByFollowingUserIdAndAcceptedIsTrue(long followingUserId);

    long countByFollowingUserIdAndAcceptedIsTrue(long followingUserId);

    List<Friend> findByFollowingUserIdAndAcceptedIsFalse(long followingUserId);

    long countByFollowingUserIdAndAcceptedIsFalse(long followingUserId);
}
