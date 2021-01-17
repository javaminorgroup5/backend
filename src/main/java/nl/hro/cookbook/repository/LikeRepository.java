package nl.hro.cookbook.repository;

import nl.hro.cookbook.model.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<List<Like>> findLikesByUserId(Long userId);
    Optional<List<Like>> findLikesByMessageId(Long messageId);
}
