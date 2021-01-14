package nl.hro.cookbook.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.Image;
import nl.hro.cookbook.model.domain.Message;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.GroupDTO;
import nl.hro.cookbook.model.mapper.GroupMapper;
import nl.hro.cookbook.repository.MessageRepository;
import nl.hro.cookbook.service.CommonService;
import nl.hro.cookbook.service.GroupService;
import nl.hro.cookbook.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/group"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;
    private final CommonService commonService;

    @GetMapping()
    public Collection<GroupDTO> getAllGroups() {
        return groupService.findAllGroup().stream()
                .map(groupMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{group_id}")
    public ResponseEntity getGroupById(@PathVariable("group_id") final long groupId) {
        Group group = groupService.findGroupById(groupId);
        if (group != null) {
            group.getImage().setPicByte(commonService.decompressBytes(group.getImage().getPicByte()));
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/create/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createGroup(@PathVariable("user_id") final long userId,
                                      @RequestPart Group group,
                                      @RequestPart("file") MultipartFile file) throws IOException {
        Image image = new Image(file.getOriginalFilename(), file.getName(),
                commonService.compressBytes(file.getBytes()));
        User user = userService.findUserById(userId);
        group.setUserId(user.getId());
        group.setImage(image);
        groupService.createGroup(group);
        return ResponseEntity.ok(group.getId());
    }

    @PostMapping("/{group_id}/generate_invite")
    public ResponseEntity generateInvite(@PathVariable("group_id") final long groupId, @RequestBody ObjectNode json) throws Exception {
        return ResponseEntity.ok(groupService.generateInvite(groupId, json.get("userId").asLong()));
    }

    @PostMapping("/{group_id}/join")
    public void joinGroup(@PathVariable("group_id") final long groupId, @RequestBody ObjectNode json) {
        groupService.joinGroup(groupId, json.get("userId").asLong(), json.get("inviteToken").asText());
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
        return ResponseEntity.badRequest().body(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{group_id}/enrolled")
    public ResponseEntity getEnrolledUsersForGroup(@PathVariable("group_id") final long groupId) {
        List<String> enrolledUsersForGroup = groupService.findEnrolledUsersForGroup(groupId);
        if (enrolledUsersForGroup.isEmpty()) {
            return ResponseEntity.badRequest().body(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(enrolledUsersForGroup);
    }

    @PostMapping("/{group_id}/feed")
    public ResponseEntity addTMessageGroupFeed(@PathVariable("group_id") final long groupId, @RequestBody Message message) {
        groupService.addMessageToFeed(groupId, message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{group_id}/feed")
    public ResponseEntity getFeedForGroup(@PathVariable("group_id") final long groupId) {
        List<Message> feedByGroupId = groupService.findFeedByGroupId(groupId);
        if (feedByGroupId.isEmpty()) {
            return ResponseEntity.badRequest().body(HttpStatus.NOT_FOUND);
        }
        feedByGroupId.forEach(message -> message.getImage().setPicByte(commonService.decompressBytes(message.getImage().getPicByte())));
        return ResponseEntity.ok(feedByGroupId);
    }

    @PutMapping(value = "/{group_id}/user/{user_id}")
    public void updateRecipe(@PathVariable("group_id") final long groupId,
                             @PathVariable("user_id") final long userId,
                             @RequestPart(value = "group", required = false) GroupDTO groupDTO) {
        userService.findUserById(userId);
        Group group = null;
        if(groupDTO != null) {
            group = groupMapper.toModel(groupDTO);
        }
        groupService.updateGroup(groupId, group);
    }

    @DeleteMapping("/{group_id}/{user_id}")
    public void deleteGroup(@PathVariable("group_id") final long groupId, @PathVariable("user_id") final long userId) {
        groupService.deleteById(groupId, userId);
    }
}
