package reviewme.be.user.service;

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

        User loggedInUser = user.orElseGet(
                () -> userRepository.save(new User(userGithubProfile)));

        return UserProfileResponse.fromUser(loggedInUser);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByStartName(String start, Pageable pageable) {

        return userRepository.findUsersByStartName(start, pageable);
    }
}
