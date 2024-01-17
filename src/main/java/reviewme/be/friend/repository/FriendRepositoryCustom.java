package reviewme.be.friend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.user.dto.UserResponse;

public interface FriendRepositoryCustom {

    Page<UserResponse> findFriendsByUserId(long userId, Pageable pageable);
}
