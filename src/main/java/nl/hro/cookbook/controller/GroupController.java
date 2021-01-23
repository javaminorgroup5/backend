package nl.hro.cookbook.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.*;
import nl.hro.cookbook.model.dto.GroupDTO;
import nl.hro.cookbook.model.mapper.GroupMapper;
import nl.hro.cookbook.service.CategoryService;
import nl.hro.cookbook.service.CommonService;
import nl.hro.cookbook.service.GroupService;
import nl.hro.cookbook.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/group"},produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;
    private final CommonService commonService;
    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.ok(groupService.findAllGroup());
    }

    /**
     * Get group by id.
     *
     * @param groupId
     * @return
     */
    @GetMapping("/{group_id}")
    public ResponseEntity<?> getGroupById(@PathVariable("group_id") final long groupId) throws Exception {
        Group group = groupService.findGroupById(groupId);
        if (group != null) {
            group.getImage().setPicByte(commonService.decompressBytes(group.getImage().getPicByte()));
            return ResponseEntity.ok(groupMapper.toDTO(group));
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    /**
     * Create a group.
     *
     * @param userId
     * @param group
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/create/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createGroup(@PathVariable("user_id") final long userId,
                                      @RequestPart Group group,
                                      @RequestPart("groupCategoryId") Long categoryId,
                                      @RequestPart("file") MultipartFile file) throws IOException {
        Image image = new Image(file.getOriginalFilename(), file.getName(),
                commonService.compressBytes(file.getBytes()));
        User user = userService.findUserById(userId);
        Category category = categoryService.findCategoryById(categoryId);
        group.setCategory(category);
        group.setUserId(user.getId());
        group.setImage(image);
        groupService.createGroup(group);
        return ResponseEntity.ok(group.getId());
    }

    /**
     * Generate de invite link for a group, so they can join the group.
     *
     * @param groupId
     * @param json
     * @return
     * @throws Exception
     */
    @PostMapping("/{group_id}/generate_invite")
    public ResponseEntity<?> generateInvite(@PathVariable("group_id") final long groupId, @RequestBody ObjectNode json) throws Exception {
        return ResponseEntity.ok(groupService.generateInvite(groupId, json.get("userId").asLong()));
    }
//TO-DO: Word deze ergens voor gebruikt?
    @PostMapping("/{group_id}/join")
    public void joinGroup(@PathVariable("group_id") final long groupId, @RequestBody ObjectNode json) throws Exception {
        long userId = json.get("userId").asLong();
        groupService.joinGroup(groupId, userId, json.get("inviteToken").asText());
        groupService.saveInviteSuccesMessageToFeed(groupId, userId);
    }

    /**
     *
     *
     * @param groupId
     * @param json
     */
    @PostMapping("/{group_id}/enroll")
    public ResponseEntity<?> enrollInGroup(@PathVariable("group_id") final long groupId, @RequestBody ObjectNode json) throws Exception {
        long userId = json.get("userId").asLong();
        groupService.enrollInGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get a single group by id.
     *
     * @param groupId
     * @param userId
     * @return
     */
    @GetMapping("/{group_id}/user/{user_id}")
    public ResponseEntity<?> getGroup(@PathVariable("group_id") final long groupId, @PathVariable("user_id") final long userId) throws Exception {
        User user = userService.findUserById(userId);
        GroupDTO group = groupMapper.toDTO(groupService.findGroupById(groupId));
        if (user.getId() == group.getUserId()) {
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{group_id}/enrolled")
    public ResponseEntity<?> getEnrolledUsersForGroup(@PathVariable("group_id") final long groupId) throws Exception {

        List<String> enrolledUsersForGroup = groupService.findEnrolledUsersForGroup(groupId);
        if (enrolledUsersForGroup.isEmpty()) {
            return ResponseEntity.badRequest().body(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(enrolledUsersForGroup);
    }

    /**
     * Add a message in the feed for the group.
     *
     * @param groupId
     * @param message
     * @return
     */
    @PostMapping("/{group_id}/feed")
    public ResponseEntity<?> addTMessageGroupFeed(@PathVariable("group_id") final long groupId, @RequestBody Message message) {
        groupService.addMessageToFeed(groupId, message);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all messages for the feed from the group.
     *
     * @param groupId
     * @return
     */
    @GetMapping("/{group_id}/feed")
    public ResponseEntity<?> getFeedForGroup(@PathVariable("group_id") final long groupId) {
        List<Message> feedByGroupId = groupService.findFeedByGroupId(groupId);
        if (feedByGroupId.isEmpty()) {
            return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
        }
        feedByGroupId.forEach(message -> message.getImage().setPicByte(commonService.decompressBytes(message.getImage().getPicByte())));
        return ResponseEntity.ok(feedByGroupId);
    }

    /**
     * Update the group.
     *
     * @param groupId
     * @param userId
     * @param groupDTO
     * @param file
     * @throws IOException
     */
    @PutMapping(value = "/{group_id}/user/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateGroup(@PathVariable("group_id") final long groupId,
                             @PathVariable("user_id") final long userId,
                             @RequestPart(value = "group", required = false) GroupDTO groupDTO,
                             @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        userService.findUserById(userId);
        Group group = null;
        if(groupDTO != null) {
            group = groupMapper.toModel(groupDTO);
        }

        // Setup the file, so we can update the file in the group.
        if (file != null && group != null) {
            Image groupImage = new Image(
                    file.getOriginalFilename(),
                    file.getName(),
                    commonService.compressBytes(file.getBytes())
            );
            group.setImage(groupImage);
        }

        // Update the group by groupId with the new values;
        groupService.updateGroup(groupId, group);
    }

    /**
     * Delete a group.
     *
     * @param groupId
     * @param userId
     */
    @DeleteMapping("/{group_id}/{user_id}")
    public void deleteGroup(@PathVariable("group_id") final long groupId, @PathVariable("user_id") final long userId) {
        groupService.deleteById(groupId, userId);
    }
}
