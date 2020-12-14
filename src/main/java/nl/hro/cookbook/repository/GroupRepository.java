package nl.hro.cookbook.repository;

import nl.hro.cookbook.model.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<List<Group>> findGroupsByUserId(Long userId);
}