package reviewme.be.util.vo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reviewme.be.util.repository.EmojiRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmojisVO {

    private final EmojiRepository emojiRepository;

    private Map<Integer, String> emojis;

    @PostConstruct
    public void init() {
        emojis = emojiRepository.findAll()
                .stream()
                .collect(
                        HashMap::new,
                        (map, emoji) -> map.put(emoji.getId(), emoji.getEmoji()),
                        HashMap::putAll
                );

        for (Map.Entry<Integer, String> entry : emojis.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
        }
    }
}
