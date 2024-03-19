package reviewme.be.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.user.dto.response.UserResponse;

public interface UserRepositoryCustom {

    Page<UserResponse> findUsersByStartName(long userId, String name, Pageable pageable);
}
