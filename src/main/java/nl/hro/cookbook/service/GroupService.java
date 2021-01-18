package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import nl.hro.cookbook.model.domain.*;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.MessageRepository;
import nl.hro.cookbook.repository.GroupRepository;
import nl.hro.cookbook.repository.InviteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final InviteRepository inviteRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final TestDataService testDataService;
    private final MessageRepository messageRepository;

    public List<Group> findAllGroup() {
        return groupRepository.findAll();
    }

    public Group findGroupById(final long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No group exists for id: %d", groupId), Group.class));
    }

    @Transactional
    public Invite generateInvite(final long groupId, long userId) throws Exception {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException(String.format("No group exists for id: %d", groupId), Group.class));
        if (group.getUserId() == userId) {
            Invite invite = new Invite(null, RandomString.make(12));
            List<Invite> invites = group.getInvites();
            invites.add(invite);
            group.setInvites(invites);
            inviteRepository.save(invite);
            groupRepository.save(group);
            return invite;
        } else {
            throw new Exception("You are not the owner of the group.");
        }
    }

    public List<String> findEnrolledUsersForGroup(long groupId) {
        List<String> userIDs = new ArrayList<>();
        Group group = findGroupById(groupId);
        for (User user : group.getEnrolledUsers()) {
            long userID = user.getId();
            userIDs.add(Long.toString(userID));
        }
        return userIDs;
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
    public void joinGroup(final long groupId, long userId, String inviteToken) {
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
    public void enrollInGroup(final long groupId, long userId) {
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

    @Transactional()
    public void updateGroup(final long groupId, final Group updateGroup) {
        Group group = findGroupById(groupId);
        if (group == null || updateGroup == null) {
            return;
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
        Optional<List<Message>> messagesByGroupId = messageRepository.findMessagesByGroupId(id);
        if (messagesByGroupId.isEmpty()) {
            return Collections.emptyList();
        }
        return messagesByGroupId.get();
    }

    @Transactional
    public void addMessageToFeed(Long groupId, Message message) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            group.getMessages().add(message);
            message.setGroupId(groupId);
            messageRepository.save(message);
            groupRepository.save(group);
        }
    }

    //    This is a pretty hacky way to have a group available on startup.
    //    This is fine for a demo, but don't do this in real code.
    @PostConstruct
    public void init() throws IOException {
        groupRepository.saveAll(testDataService.getGroups());
        messageRepository.saveAll(testDataService.getFeeds());
    }
}

