package sharva.tasklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sharva.exceptions.SharvaException;
import sharva.message.MessageMock;
import sharva.tasks.Task;
import sharva.tasks.ToDo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(tasks.get(0), messageMock.lastTask);
        assertEquals("mark", messageMock.lastAction);
    }

    @Test
    public void unmark_validIndex_success() throws SharvaException {
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
        assertEquals(tasks.get(0), messageMock.lastTask);
        assertEquals("mark", messageMock.lastAction);
        taskList.unmark(0);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        assertEquals(tasks.get(0), messageMock.lastTask);
        assertEquals("unmark", messageMock.lastAction);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 3})  // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 3 refers to 4 (this is after parsing)
    public void mark_invalidIndex_exceptionThrown(int index) {
        try {
            taskList.mark(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            assertNull(messageMock.lastTask);
            assertNull(messageMock.lastAction);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 3})  // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 3 refers to 4 (this is after parsing)
    public void unmark_invalidIndex_exceptionThrown(int index) {
        try {
            taskList.unmark(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            assertNull(messageMock.lastTask);
            assertNull(messageMock.lastAction);
        }
    }

    @Test
    public void mark_markedTask_exceptionThrown() throws SharvaException {
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
        assertEquals(tasks.get(0), messageMock.lastTask);
        assertEquals("mark", messageMock.lastAction);
        messageMock.reset();
        try {
            taskList.mark(0);
            fail();
        } catch (SharvaException e) {
            assertEquals("Completing a completed task? How hardworking", e.getMessage());
            assertNull(messageMock.lastTask);
            assertNull(messageMock.lastAction);
        }
    }

    @Test
    public void unmark_unmarkedTask_exceptionThrown() {
        try {
            taskList.unmark(0);
            fail();
        } catch (SharvaException e) {
            assertEquals("Bro, the task is not even completed", e.getMessage());
            assertNull(messageMock.lastTask);
            assertNull(messageMock.lastAction);
        }
    }

    @Test
    public void addTask_default_success() {
        taskList.addTask(new ToDo("todo3"));
        assertEquals("[T][ ] todo3", taskList.getTasks().get(2).toString());
        assertEquals(tasks.get(2), messageMock.lastTask);
        assertEquals("add", messageMock.lastAction);
    }

    @Test
    public void deleteTask_default_success() throws SharvaException {
        Task deletedTask = tasks.get(0);
        taskList.delete(0);
        assertEquals("[T][ ] todo2", taskList.getTasks().get(0).toString());
        assertEquals(deletedTask, messageMock.lastTask);
        assertEquals("delete", messageMock.lastAction);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 10})  // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 10 refers to 11 (this is after parsing)
    public void delete_invalidIndex_exceptionThrown(int index) {
        try {
            taskList.delete(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            assertNull(messageMock.lastTask);
            assertNull(messageMock.lastAction);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 10})  // indices outside range
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
            assertNull(messageMock.lastTask);
            assertNull(messageMock.lastAction);
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
        assertEquals(result.toString(), messageMock.lastInput);
        assertTrue(messageMock.isEchoCalled);
    }
}
