package reviewme.be.util.vo;

import java.util.ArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reviewme.be.util.entity.Emoji;
import reviewme.be.util.exception.NonExistEmojiException;
import reviewme.be.util.repository.EmojiRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@RequiredArgsConstructor
public class EmojisVO {

    private final EmojiRepository emojiRepository;

    private static Map<Integer, Emoji> emojis;
    private List<Emoji> emojiList;

    @PostConstruct
    public void init() {

        emojiList = emojiRepository.findAll();

        emojis = emojiList.stream()
                .collect(
                        HashMap::new,
                        (map, emoji) -> map.put(emoji.getId(), emoji),
                        HashMap::putAll
                );
    }

    public boolean validateEmojiById(int emojiId) {

        return emojis.containsKey(emojiId);
    }

    public Emoji findEmojiById(int emojiId) {

        if (!validateEmojiById(emojiId)) {
            throw new NonExistEmojiException("존재하지 않는 emojiId입니다.");
        }

        return emojis.get(emojiId);
    }

    public List<Emoji> getEmojis() {

        return new ArrayList<>(emojis.values());
    }

    public int getEmojisSize() {

        return emojis.size();
    }
}
