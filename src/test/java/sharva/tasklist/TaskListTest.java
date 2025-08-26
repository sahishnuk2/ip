package sharva.tasklist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sharva.exceptions.SharvaException;
import sharva.message.Message;
import sharva.tasks.Task;
import sharva.tasks.ToDo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void mark_validIndex_success() throws SharvaException {
        Message message = new Message();
        List<Task> tasks = List.of(new ToDo("todo1"), new ToDo("todo2"));
        TaskList taskList = new TaskList(tasks, message);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
    }

    @Test
    public void unmark_validIndex_success() throws SharvaException {
        Message message = new Message();
        List<Task> tasks = List.of(new ToDo("todo1"), new ToDo("todo2"));
        TaskList taskList = new TaskList(tasks, message);
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
        taskList.unmark(0);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 3})  // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 3 refers to 4 (this is after parsing)
    public void mark_invalidIndex_exceptionThrown(int index) {
        Message message = new Message();
        List<Task> tasks = List.of(new ToDo("todo1"), new ToDo("todo2"));
        TaskList taskList = new TaskList(tasks, message);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        try {
            taskList.mark(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 3})  // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 3 refers to 4 (this is after parsing)
    public void unmark_invalidIndex_exceptionThrown(int index) {
        Message message = new Message();
        List<Task> tasks = List.of(new ToDo("todo1"), new ToDo("todo2"));
        TaskList taskList = new TaskList(tasks, message);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        try {
            taskList.unmark(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
        }
    }

    @Test
    public void mark_markedTask_exceptionThrown() throws SharvaException {
        Message message = new Message();
        List<Task> tasks = List.of(new ToDo("todo1"), new ToDo("todo2"));
        TaskList taskList = new TaskList(tasks, message);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        taskList.mark(0);
        assertEquals("[T][X] todo1", taskList.getTasks().get(0).toString());
        try {
            taskList.mark(0);
            fail();
        } catch (SharvaException e) {
            assertEquals("Completing a completed task? How hardworking", e.getMessage());
        }
    }

    @Test
    public void unmark_unmarkedTask_exceptionThrown() {
        Message message = new Message();
        List<Task> tasks = List.of(new ToDo("todo1"), new ToDo("todo2"));
        TaskList taskList = new TaskList(tasks, message);
        assertEquals("[T][ ] todo1", taskList.getTasks().get(0).toString());
        try {
            taskList.unmark(0);
            fail();
        } catch (SharvaException e) {
            assertEquals("Bro, the task is not even completed", e.getMessage());
        }
    }

    @Test
    public void addTask_default_success() {
        Message message = new Message();
        List<Task> tasks = new ArrayList<>(List.of(new ToDo("todo1"), new ToDo("todo2")));
        // is there a better way to write this?
        TaskList taskList = new TaskList(tasks, message);
        taskList.addTask(new ToDo("todo3"));
        assertEquals("[T][ ] todo3", taskList.getTasks().get(2).toString());
    }

    @Test
    public void deleteTask_default_success() throws SharvaException {
        Message message = new Message();
        List<Task> tasks = new ArrayList<>(List.of(new ToDo("todo1"), new ToDo("todo2")));
        // is there a better way to write this?
        TaskList taskList = new TaskList(tasks, message);
        taskList.delete(0);
        assertEquals("[T][ ] todo2", taskList.getTasks().get(0).toString());
    }

    // Add test for invalid index for delete
    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 10})  // indices outside range
    // -2 -> refers tp -1, -1 refers to 0 and 10 refers to 11 (this is after parsing)
    public void delete_invalidIndex_exceptionThrown(int index) {
        try {
            Message message = new Message();
            List<Task> tasks = List.of(new ToDo("todo1"), new ToDo("todo2"));
            TaskList taskList = new TaskList(tasks, message);
            taskList.delete(index);
            fail();
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
        }
    }
}
