package reviewme.be.user.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.user.dto.UserGitHubProfile;
import reviewme.be.user.dto.response.UserProfileResponse;
import reviewme.be.user.dto.response.UserResponse;
import reviewme.be.user.exception.NonExistUserException;
import reviewme.be.user.repository.UserRepository;
import reviewme.be.user.entity.User;

import java.util.Optional;
import reviewme.be.util.exception.NotLoggedInUserException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(long id) {

        return userRepository.findById(id)
            .orElseThrow(() -> new NonExistUserException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public UserProfileResponse getUserByGithubProfile(UserGitHubProfile userGithubProfile) {

        Optional<User> user = userRepository.findByGithubId(userGithubProfile.getId());

        LocalDateTime createdAt = LocalDateTime.now();

        User loggedInUser = user.orElseGet(
                () -> userRepository.save(new User(userGithubProfile, createdAt)));

        return UserProfileResponse.fromUser(loggedInUser);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByStartName(String start, Pageable pageable) {

        return userRepository.findUsersByStartName(start, pageable);
    }

    public boolean validateLoggedInUser(User user) {

        if (user.isAnonymous()) {
            throw new NotLoggedInUserException("로그인이 필요한 서비스입니다.");
        }

        return true;
    }
}
