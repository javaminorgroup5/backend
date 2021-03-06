package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import nl.hro.cookbook.model.domain.*;
import nl.hro.cookbook.model.dto.UserDTO;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.model.mapper.UserMapper;
import nl.hro.cookbook.repository.GroupRepository;
import nl.hro.cookbook.repository.InviteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final InviteRepository inviteRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final MessageService messageService;
    private final UserMapper userMapper;

    public List<Group> findAllGroup() {
        return groupRepository.findAll();
    }

    /**
     * Find a group bij the given id;
     *
     * @param groupId
     * @return
     */

    @Transactional
    public Group findGroupById(final long groupId) throws Exception {
        Optional<Group> groupFound = groupRepository.findById(groupId);
        if (groupFound.isEmpty()) {
            throw new Exception("Group not found");
        } else {
            return groupFound.get();
        }
    }

    @Transactional
    public Invite generateInvite(final long groupId, long userId) throws Exception {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException(String.format("No group exists for id: %d", groupId), Group.class));
        if (group.getGroupPrivacy().equals(Group.GroupPrivacy.INVITE) || group.getGroupPrivacy().equals(Group.GroupPrivacy.PRIVATE)) {
            if (group.getUserId() == userId) {
                Invite invite = new Invite(RandomString.make(12));
                List<Invite> invites = group.getInvites();
                invites.add(invite);
                group.setInvites(invites);
                inviteRepository.save(invite);
                groupRepository.save(group);
                return invite;
            } else {
                throw new Exception("You are not the owner of the group.");
            }
        } else {
            throw new Exception("Group is not open to invites.");
        }
    }


    @Transactional
    public List<UserDTO> findEnrolledUsersForGroup(long groupId) throws Exception {
        List<UserDTO> userProfileNames = new ArrayList<>();
        Group group = findGroupById(groupId);
        for (User user : group.getEnrolledUsers()) {
            userProfileNames.add(userMapper.toDTO(user));
        }
        User user = userService.findUserById(group.getUserId());
        userProfileNames.add(userMapper.toDTO(user));
        return userProfileNames;
    }

    @Transactional
    public void deleteById(Long id, long userId) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            if (group.get().getUserId() == userId) {
                groupRepository.deleteById(id);
            }
        }
    }

    @Transactional
    public void joinGroup(final long groupId, long userId, String inviteToken) throws Exception {
        final User user = userService.findUserById(userId);
        Group group = findGroupById(groupId);
        List<Invite> invites = group.getInvites();
        for (Invite invite : invites) {
            String inviteTokenDB = invite.getToken();
            if (inviteTokenDB.equals(inviteToken)) {
                List<User> users = group.getEnrolledUsers();
                users.add(user);
                group.setEnrolledUsers(users);
                groupRepository.save(group);
            }
        }
    }

    @Transactional
    public void enrollInGroup(final long groupId, long userId) throws Exception {
        final User user = userService.findUserById(userId);
        Group group = findGroupById(groupId);
        List<User> users = group.getEnrolledUsers();
        users.add(user);
        group.setEnrolledUsers(users);
        groupRepository.save(group);
    }

    @Transactional
    public void createGroup(Group group) {
        groupRepository.save(group);
    }

    @Transactional
    public Optional<List<Group>> findGroupsByUserId(Long userId) {
        return groupRepository.findGroupsByUserId(userId);
    }

    @Transactional
    public void updateGroup(final long groupId, final Group updateGroup) throws Exception {
        Group group = findGroupById(groupId);
        if (group == null || updateGroup == null) {
            return;
        }
        if (updateGroup.getGroupPrivacy() != null) {
            group.setGroupPrivacy(updateGroup.getGroupPrivacy());
        }
        if (updateGroup.getGroupName() != null && !updateGroup.getGroupName().isEmpty()) {
            group.setGroupName(updateGroup.getGroupName());
        }
        if (updateGroup.getDescription() != null && !updateGroup.getDescription().isEmpty()) {
            group.setDescription(updateGroup.getDescription());
        }
        if (updateGroup.getImage() != null) {
            group.setImage(updateGroup.getImage());
        }
        groupRepository.save(group);
    }

    @Transactional
    public List<Message> findFeedByGroupId(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            return Collections.emptyList();
        }
        Optional<List<Message>> messagesByGroupId = messageService.findMessagesByGroupId(id);
        if (messagesByGroupId.isEmpty()) {
            return Collections.emptyList();
        }
        return messagesByGroupId.get();
    }

    @Transactional
    public void addMessageToGroupFeed(Long groupId, Message message) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            group.getMessages().add(message);
            message.setGroupId(groupId);
            messageService.saveMessage(message);
            groupRepository.save(group);
        }
    }

    @Transactional
    public void addMessageToGroupFeed(User user, Long groupId, Recipe recipe, Image recipeImage) throws Exception {
        Group group = this.findGroupById(groupId);
        Message message = new Message();
        message.setGroupId(group.getId());
        message.setUserId(user.getId());
        message.setImage(recipeImage);
        message.setMessage(user.getProfile().getProfileName() + " Heeft een nieuw recept toegevoegd! " + recipe.getTitle());
        message.setRecipeId(recipe.getId());
        message.setProfileName(user.getProfile().getProfileName());
        messageService.saveMessage(message);
        group.getMessages().add(message);
        groupRepository.save(group);
    }

    @Transactional
    public void saveInviteSuccesMessageToFeed(long groupId, long userId) throws Exception {
        Group group = this.findGroupById(groupId);
        User user = userService.findUserById(userId);
        Message message = new Message();
        message.setGroupId(group.getId());
        message.setUserId(user.getId());
        message.setMessage(user.getProfile().getProfileName() + " Heeft zich aangemeld voor de groep " + group.getGroupName() + "!");
        message.setProfileName(user.getProfile().getProfileName());
        message.setImage(user.getProfile().getImage());
        messageService.saveMessage(message);
        group.getMessages().add(message);
        groupRepository.save(group);
    }

}

