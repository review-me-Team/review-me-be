package reviewme.be.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.friend.repository.FriendRepository;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public boolean isFriend(long userId, long friendId) {

        return friendRepository.isFriend(userId, friendId) || friendRepository.isFriend(friendId, userId);
    }
}
