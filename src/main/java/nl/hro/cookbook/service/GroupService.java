package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import nl.hro.cookbook.model.domain.*;
<<<<<<< HEAD
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.GroupImage;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.ProfileImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.GroupRepository;
import nl.hro.cookbook.repository.InviteRepository;
import nl.hro.cookbook.repository.UserRepository;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
=======
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.GroupRepository;
import nl.hro.cookbook.repository.InviteRepository;
import nl.hro.cookbook.repository.MessageRepository;
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
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
    private final MessageService messageService;

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
<<<<<<< HEAD

=======
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
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
<<<<<<< HEAD

        for (Iterator<User> i = group.getEnrolledUsers().iterator(); i.hasNext();) {
            User item = i.next();
            String userID = item.getEmail();
            userIDs.add(userID);

=======
        for (User user : group.getEnrolledUsers()) {
            long userID = user.getId();
            userIDs.add(Long.toString(userID));
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
        }
        return userIDs;
    }

<<<<<<< HEAD
    @Transactional()
    public void deleteById(Long id, long userId) {
        Optional<Group> group = groupRepository.findById(id);

        if (group != null) {
=======
    @Transactional
    public void deleteById(Long id, long userId) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
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
<<<<<<< HEAD

        for (Invite invite : invites) {
            String inviteTokenDB = invite.getToken();

=======
        for (Invite invite : invites) {
            String inviteTokenDB = invite.getToken();
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
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

<<<<<<< HEAD
    //    This is a pretty hacky way to have a group available on startup.
    //    This is fine for a demo, but don't do this in real code.
    @PostConstruct
    public void init() throws IOException {

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:group.jpg");
        GroupImage groupImage = new GroupImage("group.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource.getFile().toPath())));

        final Group initialGroup1 = new Group(1L, "PastaGroep", "Leuke pasta groep", 1L, new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup2 = new Group(2L, "RodeSauzen", "Roder dan rood", 1L, new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup3 = new Group(3L, "Bloemkoollovers", "Bloemkool is een groente die hoort bij het geslacht kool uit de kruisbloemenfamilie (Brassicaceae). De botanische naam voor bloemkool is Brassica oleracea convar. ", 2L, new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup4 = new Group(4L, "Italiaanse keukengroep", "De Italiaanse keuken omvat de inheemse kookkunst van het Italiaanse schiereiland. Deze keuken is zeer gevarieerd en seizoensgebonden.", 2L, new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup5 = new Group(5L, "Marokkaanse keuken", "Couscous Habibi", 2L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), groupImage);
        groupRepository.saveAll(Arrays.asList(initialGroup1, initialGroup2, initialGroup3, initialGroup4, initialGroup5));
    }



    @Transactional()
=======
    @Transactional
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
    public void updateGroup(final long groupId, final Group updateGroup) {
        Group group = findGroupById(groupId);
        if (group == null || updateGroup == null) {
            return;
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
    public void addMessageToFeed(Long groupId, Message message) {
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
    public void saveMessageToGroup(User user, Optional<List<Group>> groups, Recipe recipe, Image recipeImage) {
        if (groups.isPresent()) {
            for (Group group : groups.get()) {
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
        }
    }

    @Transactional
    public void saveInviteSuccesMessageToFeed(long groupId, long userId) {
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


    //    This is a pretty hacky way to have a group available on startup.
    //    This is fine for a demo, but don't do this in real code.
    @PostConstruct
    public void init() throws IOException {
        groupRepository.saveAll(testDataService.getGroups());
        messageRepository.saveAll(testDataService.getFeeds());
    }
}
