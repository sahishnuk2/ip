package sharva.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import sharva.exceptions.SharvaException;
import sharva.tasklist.TaskListStub;
import sharva.tasks.Deadline;
import sharva.tasks.Event;
import sharva.tasks.ToDo;

/**
 * Test class for Parser functionality.
 * Tests the parsing of various user input commands and date/time formats.
 */
public class ParserTest {
    private TaskListStub stub;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        stub = new TaskListStub();
        parser = new Parser(stub);
    }

    @Test
    public void add_validTask_taskAdded() throws SharvaException {
        parser.parseInput("todo read book");

        List<String> calls = stub.getCalledMethods();
        assertEquals(1, calls.size());
        assertEquals("add:" + new ToDo("read book"), calls.get(0));

        parser.parseInput("deadline deadline /by 12122025");

        assertEquals(2, calls.size());
        assertEquals("add:" + new Deadline(
                "deadline",
                LocalDateTime.of(2025, 12, 12, 23, 59)),
                calls.get(1));

        parser.parseInput("event event /from 12122025 /to 13122025");

        assertEquals(3, calls.size());
        assertEquals("add:" + new Event(
                "event",
                LocalDateTime.of(2025, 12, 12, 0, 0),
                LocalDateTime.of(2025, 12, 13, 23, 59)
        ), calls.get(2));
    }

    @ParameterizedTest
    @ValueSource(strings = {"todo", "deadline", "event", "todo  ", "deadline  ", "event  "})
    public void add_invalidTask_exceptionThrown(String input) {
        try {
            parser.parseInput(input);
        } catch (SharvaException e) {
            assertEquals("The description of a " + input.trim() + " cannot be empty", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"ls", "toodo", "leo", "thomas  ", "hi bro", "booye"})
    public void add_invalidInput_exceptionThrown(String input) {
        try {
            parser.parseInput(input);
        } catch (SharvaException e) {
            assertEquals("Sorry bro, I don't know what that means!", e.getMessage());
        }
    }

    @Test
    public void list_default_callsListMethod() throws SharvaException {
        parser.parseInput("list");
        List<String> calls = stub.getCalledMethods();
        assertEquals(1, calls.size());
        assertEquals("list", calls.get(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void mark_valid_taskMarked(int index) throws SharvaException {
        parser.parseInput("mark " + index);

        List<String> calls = stub.getCalledMethods();
        assertEquals(1, calls.size());
        assertEquals("mark:" + index, calls.get(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void mark_invalidTaskNumber_exceptionThrown(int index) {
        try {
            parser.parseInput("mark " + index);
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
            // Exception is thrown in parser class during thr regex checking
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void unmark_invalidTaskNumber_exceptionThrown(int index) {
        try {
            parser.parseInput("unmark " + index);
        } catch (SharvaException e) {
            assertEquals("Invalid task number!", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"mark1", "mark  1", "unmark", "unmark1"})
    public void markAndUnmark_invalidTaskNumber_exceptionThrown(String str) {
        assertThrows(SharvaException.class, () -> parser.parseInput(str));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void unmark_valid_taskMarked(int index) throws SharvaException {
        parser.parseInput("unmark " + index);

        List<String> calls = stub.getCalledMethods();
        assertEquals(1, calls.size());
        assertEquals("unmark:" + index, calls.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"02122003", "02/12/2003", "02-12-2003", "2/12/2003", "2/12/03", "021203"})
    public void parseDate_validInput_correctDateTime(String input) throws SharvaException {
        LocalDate date = Parser.parseDate(input);
        assertEquals(LocalDate.of(2003, 12, 2), date);
    }

    @ParameterizedTest
    @ValueSource(strings = {"29/13/2025", "057225", "tmr"})
    public void parseDate_invalidInput_exceptionThrown(String input) {
        try {
            LocalDate date = Parser.parseDate(input);
        } catch (SharvaException e) {
            assertEquals("Date format is incorrect", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"1900", "19:00", "7.00pm", "7:00 pm", "7:00pm", "7.00 pm"})
    public void parseTime_validInput_correctDateTime(String input) throws SharvaException {
        LocalTime time = Parser.parseTime(input);
        assertEquals(LocalTime.of(19, 0), time);
    }

    @ParameterizedTest
    @ValueSource(strings = {"7pm", "21:00 pm", "1:62pm", "24"})
    public void parseTime_invalidInput_exceptionThrown(String input) {
        try {
            Parser.parseTime(input);
        } catch (SharvaException e) {
            assertEquals("Time format is incorrect", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"02122003", "02/12/2003", "02-12-2003", "2/12/2003", "2/12/03", "021203"})
    public void parseDateTime_validInputWithoutTime_correctDateTime(String input) throws SharvaException {
        LocalDateTime dateTime = Parser.parseDateTime(input, true);
        assertEquals(LocalDateTime.of(2003, 12, 2, 23, 59), dateTime);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "02122003 2119", "02/12/2003 9.19 pm", "02-12-2003 9:19pm",
        "2/12/2003 9.19pm", "2/12/03 9.19pm", "021203 21:19"
    })
    public void parseDateTime_validInputWithTime_correctDateTime(String input) throws SharvaException {
        LocalDateTime dateTime = Parser.parseDateTime(input, true);
        assertEquals(LocalDateTime.of(2003, 12, 2, 21, 19), dateTime);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "02122003 211", "02/12/2003 9.1 pm", "02-1-2003 9:9pm",
        "2/12/203 9.19m", "212/03 9.19pm", "021203 2:19"
    })
    public void parseDateTime_invalidInputWithTime_exceptionThrown(String input) throws SharvaException {
        assertThrows(SharvaException.class, () -> Parser.parseDateTime(input, true));
    }
}
