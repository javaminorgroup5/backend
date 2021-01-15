package nl.hro.cookbook.service;

import nl.hro.cookbook.model.domain.Like;
import nl.hro.cookbook.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    private final long userId = 1L;
    private final long messageId = 2L;
    private final long recipeId = 3L;
    private final Like like = new Like(userId, messageId, recipeId);

    @Test
    void saveLikeTest()  {
        // Given
        when(likeRepository.findLikesByMessageId(messageId)).thenReturn(Optional.of(Collections.singletonList(like)));

        // When
        likeService.saveLike(like);

        // Then
        List<Like> likes = likeService.findLikesByMessageId(messageId);
        assertFalse(likes.isEmpty());
        assertEquals(like, likes.get(0));
    }

    @Test
    void deleteLikeTest()  {
        // Given
        when(likeRepository.findLikesByMessageId(messageId))
                .thenReturn(Optional.of(Collections.singletonList(like)))
                .thenReturn(Optional.empty());

        // When
        likeService.saveLike(like);
        // save twice
        likeService.saveLike(like);

        // verify
        verify(likeRepository, times(2)).save(like);
        verify(likeRepository, times(1)).delete(like);

        // Then
        List<Like> likes = likeService.findLikesByMessageId(messageId);
        assertTrue(likes.isEmpty());
    }
}
