package nl.hro.cookbook.service;

import nl.hro.cookbook.model.domain.Image;
import nl.hro.cookbook.model.domain.Message;
import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.repository.MessageRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
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
    private MessageRepository messageRepository;
    @Mock
    private MessageService messageService;
    @InjectMocks
    private GroupService groupServiceTest;

    private List<Group> groups;
    private List<Message> messages;

    @BeforeEach
    void setUp() {
        Image image = new Image("group.jpg", "file", new byte[12]);
        final Group initialGroup1 = new Group(1L, "PastaGroep", "Leuke pasta groep", 1L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        final Group initialGroup2 = new Group(2L, "RodeSauzen", "Roder dan rood", 1L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        final Group initialGroup3 = new Group(3L, "Bloemkoollovers", "Bloemkool is een groente die hoort bij het geslacht kool uit de kruisbloemenfamilie (Brassicaceae). De botanische naam voor bloemkool is Brassica oleracea convar. ", 2L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        final Group initialGroup4 = new Group(4L, "RamsayItes", "Koken net Gordon Ramsay!. ", 3L, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        final Message message1 = new Message("This is my first message", 3L, 6L, "Test", 1L, image);
        final Message message2 = new Message("This is my second message", 3L, 6L, "Test", 1L, image);
        final Message message3 = new Message("This is my third message", 3L, 6L, "Test", 1L, image);
        groups = Arrays.asList(initialGroup1, initialGroup2, initialGroup3, initialGroup4);
        messages = Arrays.asList(message1, message2, message3);
    }

    @Test
    void addMessageToFeedTest() {
        // Given
        when(groupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(groups.get(0)));
        when(messageService.findMessagesByGroupId(anyLong())).thenReturn(Optional.of(messages));

        // When
        groupServiceTest.addMessageToFeed(groups.get(0).getId(), messages.get(0));

        // Then
        List<Message> messageList = groupServiceTest.findFeedByGroupId(1L);
        assertFalse(messageList.isEmpty());
        assertEquals(messageList.get(0).getMessage(), "This is my first message");
    }
}
