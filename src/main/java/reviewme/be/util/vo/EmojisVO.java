package reviewme.be.util.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reviewme.be.util.entity.Emoji;
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

    private Map<Integer, String> emojis;
    private List<Emoji> emojiList;

    @PostConstruct
    public void init() {

        emojiList = emojiRepository.findAll();

        emojis = emojiList.stream()
                .collect(
                        HashMap::new,
                        (map, emoji) -> map.put(emoji.getId(), emoji.getEmoji()),
                        HashMap::putAll
                );
    }
}
