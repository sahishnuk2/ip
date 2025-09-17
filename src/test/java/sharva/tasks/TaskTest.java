package sharva.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import sharva.exceptions.InvalidArgumentsException;
import sharva.exceptions.SharvaException;



/**
 * Test class for Task functionality.
 * Tests the basic operations of Task subclasses including ToDo, Deadline, and Event.
 */
public class TaskTest {

    @Test
    public void toString_defaultTask_success() throws SharvaException {
        ToDo todo = new ToDo("taskname");
        assertEquals("[T][ ] taskname", todo.toString());
        todo.markAsDone();
        assertEquals("[T][X] taskname", todo.toString());

        Deadline deadline = new Deadline("taskname", LocalDateTime.of(2025, 12, 5, 7, 19));
        assertEquals("[D][ ] taskname (by: 05/12/2025 07:19)", deadline.toString());

        Event event = new Event("taskname", LocalDateTime.of(2025, 12, 2, 7, 0), LocalDateTime.of(2025, 12, 2, 8, 0));
        assertEquals("[E][ ] taskname (from: 02/12/2025 07:00 to: 02/12/2025 08:00)", event.toString());
    }

    @Test
    public void toSaveString_defaultTask_success() throws SharvaException {
        ToDo todo = new ToDo("taskname");
        assertEquals("T @@@ 0 @@@ taskname", todo.toSaveString());
        todo.markAsDone();
        assertEquals("T @@@ 1 @@@ taskname", todo.toSaveString());

        Deadline deadline = new Deadline("taskname", LocalDateTime.of(2025, 12, 5, 7, 19));
        assertEquals("D @@@ 0 @@@ taskname @@@ 05/12/2025 0719", deadline.toSaveString());

        Event event = new Event("taskname", LocalDateTime.of(2025, 12, 2, 7, 0), LocalDateTime.of(2025, 12, 2, 8, 0));
        assertEquals("E @@@ 0 @@@ taskname @@@ 02/12/2025 0700 @@@ 02/12/2025 0800", event.toSaveString());
    }

    @Test
    public void markTask_unmarkedTask_success() throws SharvaException {
        ToDo todo = new ToDo("taskname");
        todo.markAsDone();
        assertEquals("[T][X] taskname", todo.toString());
    }

    @Test
    public void unmarkTask_markedTask_success() throws SharvaException {
        ToDo todo = new ToDo("taskname");
        todo.markAsDone();
        todo.undoTask();
        assertEquals("[T][ ] taskname", todo.toString());
    }

    @Test
    public void markTask_unmarkedTask_exceptionThrown() throws SharvaException {
        ToDo todo = new ToDo("taskname");
        todo.markAsDone();
        try {
            todo.markAsDone();
        } catch (InvalidArgumentsException e) {
            assertEquals("Completing a completed task? How hardworking", e.getMessage());
        }
        assertEquals("[T][X] taskname", todo.toString());
    }

    @Test
    public void unmarkTask_markedTask_exceptionThrown() throws SharvaException {
        ToDo todo = new ToDo("taskname");
        try {
            todo.undoTask();
        } catch (InvalidArgumentsException e) {
            assertEquals("Bro, the task is not even completed", e.getMessage());
        }
        assertEquals("[T][ ] taskname", todo.toString());
    }
}
