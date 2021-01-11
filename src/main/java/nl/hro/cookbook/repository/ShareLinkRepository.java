package nl.hro.cookbook.repository;

import nl.hro.cookbook.model.domain.Invite;
import nl.hro.cookbook.model.domain.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {
    Optional<ShareLink> findShareLinkByShareLink(String shareLink);
}