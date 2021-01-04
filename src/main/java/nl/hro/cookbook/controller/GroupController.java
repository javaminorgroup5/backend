package nl.hro.cookbook.controller;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.GroupImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.GroupDTO;
import nl.hro.cookbook.model.mapper.GroupMapper;
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
            group.getGroupImage().setPicByte(commonService.decompressBytes(group.getGroupImage().getPicByte()));
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.badRequest().body(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/create/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createGroup(@PathVariable("user_id") final long userId,
                                      @RequestPart Group group,
                                      @RequestPart("file") MultipartFile file) throws IOException {
        GroupImage groupImage = new GroupImage(file.getOriginalFilename(), file.getName(),
                commonService.compressBytes(file.getBytes()));
        User user = userService.findUserById(userId);
        group.setUserId(user.getId());
        group.setGroupImage(groupImage);
        groupService.createGroup(group);
        return ResponseEntity.ok(group.getId());
    }

    @PostMapping("/{group_id}/join")
    public void joinGroup(@PathVariable("group_id") final long groupId, @RequestBody Long userId) {
        groupService.joinGroup(groupId, userId);
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
}
