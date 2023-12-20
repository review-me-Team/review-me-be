package reviewme.be.util.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reviewme.be.util.entity.Emoji;

import java.util.List;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {

    List<Emoji> findAll();
}
