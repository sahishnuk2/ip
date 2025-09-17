package sharva.tasklist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import sharva.exceptions.SharvaException;
import sharva.message.MessageMock;
import sharva.tasks.Task;
import sharva.tasks.ToDo;

/**
 * Test class for TaskList functionality.
 * Tests task management operations including adding, deleting, and marking tasks.
 */
public class TaskListTest {

    private TaskList taskList;
    private List<Task> tasks;
    private MessageMock messageMock;

    @BeforeEach
    public void setup() {
        messageMock = new MessageMock();
        tasks = new ArrayList<>(List.of(new ToDo("todo1"), new ToDo("todo2")));
        taskList = new TaskList(tasks, messageMock);
    }

    @Test
    public void mark_validIndex_success() throws SharvaException {
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
        assertEquals(tasks.get(0), messageMock.getLastTask());
        assertEquals("mark", messageMock.getLastAction());
    }

    @Test
    public void unmark_validIndex_success() throws SharvaException {
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
        assertEquals(tasks.get(0), messageMock.getLastTask());
        assertEquals("mark", messageMock.getLastAction());
        taskList.unmark(0);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        assertEquals(tasks.get(0), messageMock.getLastTask());
        assertEquals("unmark", messageMock.getLastAction());
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 3}) // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 3 refers to 4 (this is after parsing)
    public void mark_invalidIndex_exceptionThrown(int index) {
        try {
            taskList.mark(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            assertNull(messageMock.getLastTask());
            assertNull(messageMock.getLastAction());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 3}) // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 3 refers to 4 (this is after parsing)
    public void unmark_invalidIndex_exceptionThrown(int index) {
        try {
            taskList.unmark(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            assertNull(messageMock.getLastTask());
            assertNull(messageMock.getLastAction());
        }
    }

    @Test
    public void mark_markedTask_exceptionThrown() throws SharvaException {
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
        assertEquals(tasks.get(0), messageMock.getLastTask());
        assertEquals("mark", messageMock.getLastAction());
        messageMock.reset();
        try {
            taskList.mark(0);
            fail();
        } catch (SharvaException e) {
            assertEquals("Completing a completed task? How hardworking", e.getMessage());
            assertNull(messageMock.getLastTask());
            assertNull(messageMock.getLastAction());
        }
    }

    @Test
    public void unmark_unmarkedTask_exceptionThrown() {
        try {
            taskList.unmark(0);
            fail();
        } catch (SharvaException e) {
            assertEquals("Bro, the task is not even completed", e.getMessage());
            assertNull(messageMock.getLastTask());
            assertNull(messageMock.getLastAction());
        }
    }

    @Test
    public void addTask_default_success() {
        taskList.addTask(new ToDo("todo3"));
        assertEquals("[T][ ] todo3", taskList.getTasks().get(2).toString());
        assertEquals(tasks.get(2), messageMock.getLastTask());
        assertEquals("add", messageMock.getLastAction());
    }

    @Test
    public void deleteTask_default_success() throws SharvaException {
        Task deletedTask = tasks.get(0);
        taskList.delete(0);
        assertEquals("[T][ ] todo2", taskList.getTasks().get(0).toString());
        assertEquals(deletedTask, messageMock.getLastTask());
        assertEquals("delete", messageMock.getLastAction());
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 10}) // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 10 refers to 11 (this is after parsing)
    public void delete_invalidIndex_exceptionThrown(int index) {
        try {
            taskList.delete(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            assertNull(messageMock.getLastTask());
            assertNull(messageMock.getLastAction());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 10}) // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 10 refers to 11 (this is after parsing)
    public void delete_emptyList_exceptionThrown(int index) throws SharvaException {
        taskList.delete(0);
        taskList.delete(0);
        // Initially there are 2 element, I am deleting them
        messageMock.reset();
        try {
            taskList.delete(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            assertNull(messageMock.getLastTask());
            assertNull(messageMock.getLastAction());
        }
    }

    // add a test for list -> should check for input string and also if last action called is echo.
    @Test
    public void list_default_success() {
        StringBuilder result = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("\n").append(String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }

        taskList.list();
        assertEquals(result.toString(), messageMock.getLastInput());
        assertTrue(messageMock.getIsEchoCalled());
    }

    @Test
    public void find_default_success() {
        StringBuilder result = new StringBuilder("Here are the tasks in your list:");
        result.append("\n").append(String.format("%d. %s", 1, tasks.get(0).toString()));

        taskList.find("todo1");
        assertEquals(result.toString(), messageMock.getLastInput());
        assertTrue(messageMock.getIsEchoCalled());
    }
}
