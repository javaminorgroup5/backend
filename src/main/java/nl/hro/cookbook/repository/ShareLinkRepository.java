package nl.hro.cookbook.repository;

<<<<<<< HEAD
import nl.hro.cookbook.model.domain.Invite;
=======
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
import nl.hro.cookbook.model.domain.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {
    Optional<ShareLink> findShareLinkByShareLink(String shareLink);
}
