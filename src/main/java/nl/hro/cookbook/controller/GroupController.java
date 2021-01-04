package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.GroupDTO;
import nl.hro.cookbook.model.mapper.GroupMapper;
import nl.hro.cookbook.service.GroupService;
import nl.hro.cookbook.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/group"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;

    @GetMapping()
    public Collection<GroupDTO> getAllGroups() {
        return groupService.findAllGroup().stream()
                .map(groupMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{group_id}")
    public ResponseEntity getGroupById(@PathVariable("group_id") final long groupId) {
        GroupDTO group = groupMapper.toDTO(groupService.findGroupById(groupId));
        if (group != null) {
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create/{user_id}")
    public ResponseEntity createGroup(@PathVariable("user_id") final long userId, @RequestBody Group group) {
        User user = userService.findUserById(userId);
        group.setUserId(user.getId());
        groupService.createGroup(group);
        return ResponseEntity.ok(group.getId());
    }

    @PostMapping("/{group_id}/join")
    public void joinGroup(@PathVariable("group_id") final long groupId, @RequestBody Long userId) {
        groupService.joinGroup(groupId, userId);
    }

    @PostMapping("/{group_id}/enroll")
    public void enrollInGroup(@PathVariable("group_id") final long groupId, @RequestBody Long userId) {
        groupService.enrollInGroup(groupId, userId);
    }

    @GetMapping("/{group_id}/user/{user_id}")
    public ResponseEntity getGroup(@PathVariable("group_id") final long groupId, @PathVariable("user_id") final long userId) {
        User user = userService.findUserById(userId);
        GroupDTO group = groupMapper.toDTO(groupService.findGroupById(groupId));
        if (user.getId() == group.getUserId()) {
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{group_id}/enrolled")
    public ResponseEntity getEnrolledUsersForGroup(@PathVariable("group_id") final long groupId) {
        return ResponseEntity.ok(groupService.findEnrolledUsersForGroup(groupId));
    }
}
