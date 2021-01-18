package nl.hro.cookbook.repository;

import nl.hro.cookbook.model.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findMessagesByGroupId(Long groupId);
    Optional<List<Message>> findMessagesByUserId(Long userId);
}
