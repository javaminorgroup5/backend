package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Like;
import nl.hro.cookbook.model.domain.Message;
import nl.hro.cookbook.repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    public List<Like> findLikesByMessageId(final long messageId) {
        Optional<List<Like>> likes = likeRepository.findLikesByMessageId(messageId);
        if (likes.isEmpty()) {
            return Collections.emptyList();
        }
        return likes.get();
    }

    public void saveLike(Like like) {
        List<Like> likesByMessageId = findLikesByMessageId(like.getMessageId());
        Optional<Like> optionalLike = likesByMessageId.stream()
                .filter(like1 -> like1.getUserId() == like.getUserId())
                .findFirst();
        if (like.getMessageId() > 0 && optionalLike.isPresent()) {
            likeRepository.delete(optionalLike.get());
        }
        likeRepository.save(like);
    }
}
