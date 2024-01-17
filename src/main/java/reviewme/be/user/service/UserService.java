package reviewme.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.user.exception.NonExistUserException;
import reviewme.be.user.repository.UserRepository;
import reviewme.be.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User getUserById(long id) {

        return userRepository.findById(id)
            .orElseThrow(() -> new NonExistUserException("[ERROR] 존재하지 않는 유저입니다."));
    }


}
