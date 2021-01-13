package nl.hro.cookbook.service;

import nl.hro.cookbook.model.domain.GroupImage;
import nl.hro.cookbook.model.domain.Message;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.repository.FeedRepository;
import nl.hro.cookbook.repository.GroupRepository;
import nl.hro.cookbook.repository.InviteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private InviteRepository inviteRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserService userService;
    @Mock
    private FeedRepository feedRepository;
    @InjectMocks
    private GroupService groupServiceTest;

    private List<Group> groups;
    private List<Message> messages;

    @BeforeEach
    void setUp() {
        final Message message1 = new Message(1L, "This is my first message", 3L);
        final Message message2 = new Message(2L, "This is my second message", 3L);
        final Message message3 = new Message(3L, "This is my third message", 3L);
        GroupImage groupImage = new GroupImage("group.jpg", "file", new byte[12]);
        final Group initialGroup1 = new Group(1L, "PastaGroep", "Leuke pasta groep", 1L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup2 = new Group(2L, "RodeSauzen", "Roder dan rood", 1L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), groupImage);
        final Group initialGroup3 = new Group(3L, "Bloemkoollovers", "Bloemkool is een groente die hoort bij het geslacht kool uit de kruisbloemenfamilie (Brassicaceae). De botanische naam voor bloemkool is Brassica oleracea convar. ", 2L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), groupImage);
        groups = Arrays.asList(initialGroup1, initialGroup2, initialGroup3);
        messages = Arrays.asList(message1, message2, message3);
    }


    @Test
    void addMessageToFeedTest() {
        // Given
        when(groupRepository.findById(eq(1L))).thenReturn(Optional.ofNullable(groups.get(0)));

        // When
        groupServiceTest.addMessageToFeed(groups.get(0).getId(), messages.get(0));

        // Then
        List<Message> messageList = groupServiceTest.findFeedByGroupId(1L);
        assertFalse(messageList.isEmpty());
        assertEquals(messageList.get(0).getMessage(), "This is my first message");
    }


}
