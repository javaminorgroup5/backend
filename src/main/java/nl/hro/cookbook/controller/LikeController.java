package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Like;
import nl.hro.cookbook.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/likes"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/message/{message_id}")
    public ResponseEntity getLikeByMessageId(@PathVariable("message_id") final long messageId) {
        List<Like> likes = likeService.findLikesByMessageId(messageId);
        if (likes.isEmpty()) {
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
        return ResponseEntity.ok(likes);
    }

    @PostMapping("/message")
    public ResponseEntity saveLike(@RequestBody final Like like) { likeService.saveLike(like);
        return ResponseEntity.ok(like.getId());
    }
}
