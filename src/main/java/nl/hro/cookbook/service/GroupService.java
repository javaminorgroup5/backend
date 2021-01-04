package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.domain.GroupImage;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.ProfileImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.exception.ResourceNotFoundException;
import nl.hro.cookbook.repository.GroupRepository;
import nl.hro.cookbook.repository.UserRepository;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final CommonService commonService;

    public List<Group> findAllGroup() {
        return groupRepository.findAll();
    }

    public Group findGroupById(final long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No group exists for id: %d", groupId), Group.class));
    }

    public void joinGroup(final long groupId, long userId) {
        final User user = userService.findUserById(userId);
        Group group = findGroupById(groupId);
        List<Profile> profiles = group.getProfiles();
        profiles.add(user.getProfile());
        group.setProfiles(profiles);
        groupRepository.save(group);
    }

    @Transactional()
    public void createGroup(Group group) {
        groupRepository.save(group);
    }

    public Optional<List<Group>> findGroupsByUserId(Long userId) {
        return groupRepository.findGroupsByUserId(userId);
    }

    //    This is a pretty hacky way to have a group available on startup.
    //    This is fine for a demo, but don't do this in real code.
    @PostConstruct
    public void init() throws IOException {

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:group.jpg");
        GroupImage groupImage = new GroupImage("group.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource.getFile().toPath())));

        final Group initialGroup1 = new Group(1L, "PastaGroep", "Leuke pasta groep", 1L, new ArrayList<>(), groupImage);
        final Group initialGroup2 = new Group(2L, "RodeSauzen", "Roder dan rood", 1L, new ArrayList<>(), groupImage);
        final Group initialGroup3 = new Group(3L, "Bloemkoollovers", "Bloemkool is een groente die hoort bij het geslacht kool uit de kruisbloemenfamilie (Brassicaceae). De botanische naam voor bloemkool is Brassica oleracea convar. ", 2L, new ArrayList<>(), groupImage);
        final Group initialGroup4 = new Group(4L, "Italiaanse keukengroep", "De Italiaanse keuken omvat de inheemse kookkunst van het Italiaanse schiereiland. Deze keuken is zeer gevarieerd en seizoensgebonden.", 2L, new ArrayList<>(), groupImage);
        groupRepository.saveAll(Arrays.asList(initialGroup1, initialGroup2, initialGroup3, initialGroup4));
    }

    @Transactional()
    public void updateGroup(final long groupId, final Group updateGroup) {
        Group group = findGroupById(groupId);
        if (group == null || updateGroup == null) {
            return;
        }
        groupRepository.save(group);
    }
}

