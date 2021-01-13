package nl.hro.cookbook.repository;

import nl.hro.cookbook.model.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Message, Long> { }
