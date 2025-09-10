package sharva.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import sharva.exceptions.InvalidArgumentsException;
import sharva.exceptions.InvalidCommandException;
import sharva.exceptions.InvalidIndexException;
import sharva.exceptions.SharvaException;
import sharva.tasklist.TaskListService;
import sharva.tasks.Deadline;
import sharva.tasks.Event;
import sharva.tasks.Task;
import sharva.tasks.ToDo;

/**
 * Handles user input
 */
public class Parser {
    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("dd-M-yyyy"),
            DateTimeFormatter.ofPattern("dd-M-yy"),
            DateTimeFormatter.ofPattern("d-M-yy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd/M/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd/M/yy"),
            DateTimeFormatter.ofPattern("d/M/yy"),
            DateTimeFormatter.ofPattern("ddMMyyyy"),
            DateTimeFormatter.ofPattern("ddMMyy")
    );

    private static final List<DateTimeFormatter> TIME_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("HHmm"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("h:mm a"),
            DateTimeFormatter.ofPattern("h.mm a"),
            DateTimeFormatter.ofPattern("h:mma"),
            DateTimeFormatter.ofPattern("h.mma")
    );

    // Command delimiters
    private static final String BY_DELIMITER = " /by ";
    private static final String FROM_DELIMITER = " /from ";
    private static final String TO_DELIMITER = " /to ";

    // Prefix lengths for command parsing
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 8;
    private static final int EVENT_PREFIX_LENGTH = 5;

    // Delimiter lengths for substring operations
    private static final int BY_DELIMITER_LENGTH = 5;
    private static final int FROM_DELIMITER_LENGTH = 7;
    private static final int TO_DELIMITER_LENGTH = 5;

    private final TaskListService tasks;

    public Parser(TaskListService tasks) {
        this.tasks = tasks;
    }

    /**
     * Parses the user input string into a LocalDate.
     *
     * @param date the user input string
     * @return the corresponding LocalDate object
     * @throws SharvaException if the input format is incorrect
     */
    public static LocalDate parseDate(String date) throws SharvaException {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }
        // If not successful
        throw new InvalidArgumentsException("Date format is incorrect");
    }

    /**
     * Parses the user input string into a LocalTime.
     *
     * @param time the user input string
     * @return the corresponding LocalTime object
     * @throws SharvaException if the input format is incorrect
     */
    public static LocalTime parseTime(String time) throws SharvaException {
        for (DateTimeFormatter formatter : TIME_FORMATTERS) {
            try {
                return LocalTime.parse(time.toLowerCase(), formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }
        throw new InvalidArgumentsException("Time format is incorrect");
    }

    /**
     * Parses the user input string into a LocalDateTime.
     * <p>
     * If the input contains a date, the isEnd parameter determines
     * the time of day: 23:59 if true, or 00:00 if false.
     * If the input does not contain a date, isEnd parameter is ignored.
     * @param input the user input string
     * @param isEnd indicates whether the time represents the end of a period
     * @return the corresponding LocalDateTime object
     * @throws SharvaException if input format is incorrect
     */
    public static LocalDateTime parseDateTime(String input, boolean isEnd) throws SharvaException {
        String[] parts = input.split(" ");

        LocalDate date = extractDate(parts);
        LocalTime time = extractTime(parts);

        if (time == null) {
            time = getDefaultTime(isEnd);
        }

        return LocalDateTime.of(date, time);
    }

    private static LocalDate extractDate(String[] parts) throws SharvaException {
        if (parts.length == 0) {
            throw new InvalidArgumentsException("Invalid date and time format");
        }
        return parseDate(parts[0]);
    }

    private static LocalTime extractTime(String[] parts) throws SharvaException {
        switch (parts.length) {
        case 1:
            return null; // No time specified
        case 2:
            return parseTime(parts[1]);
        case 3:
            return parseTime(parts[1] + " " + parts[2]);
        default:
            throw new InvalidArgumentsException("Invalid date and time format");
        }
    }

    private static LocalTime getDefaultTime(boolean isEnd) {
        return isEnd ? LocalTime.of(23, 59) : LocalTime.of(0, 0);
    }

    /**
     * Extracts and validates a task description from a substring operation.
     * @param input the full input string
     * @param startIndex the starting index for substring
     * @param endIndex the ending index for substring
     * @param errorMessage the error message to throw if description is empty
     * @return the trimmed task description
     * @throws InvalidArgumentsException if the description is empty
     */
    private String extractTaskDescription(
            String input, int startIndex, int endIndex, String errorMessage) throws InvalidArgumentsException {
        String description = input.substring(startIndex, endIndex).trim();
        if (description.isEmpty()) {
            throw new InvalidArgumentsException(errorMessage);
        }
        return description;
    }

    /**
     * Parses the user input string and delegates to the appropriate command method.
     * <p>
     * Recognised commands include: list, mark, unmark, add, delete, todo, deadline,
     * and event.
     * @param input the user input string
     * @throws SharvaException if the input format is incorrect
     */
    public void parseInput(String input) throws SharvaException {
        if (input.equals("list")) {
            list();
        } else if (input.startsWith("mark")) {
            mark(input);
        } else if (input.startsWith("unmark")) {
            unmark(input);
        } else if (input.startsWith("delete")) {
            delete(input);
        } else if (input.startsWith("todo ")) {
            toDo(input);
        } else if (input.startsWith("deadline ")) {
            deadline(input);
        } else if (input.startsWith("event ")) {
            event(input);
        } else if (input.startsWith("find ")) {
            find(input);
        } else {
            handleInvalidInput(input);
        }
    }

    private void handleInvalidInput(String input) throws SharvaException {
        if (input.equals("todo") || input.equals("deadline") || input.equals("event") || input.equals("find")) {
            throw new InvalidArgumentsException("The description of a " + input + " cannot be empty");
        }
        throw new InvalidCommandException();
    }

    private void list() {
        tasks.list();
    }

    private void find(String input) throws SharvaException {
        validateFindInput(input);
        String findItem = input.substring(5).trim();
        tasks.find(findItem);
    }

    private void validateFindInput(String input) throws InvalidArgumentsException {
        if (input.trim().equals("find")) {
            throw new InvalidArgumentsException("The description of a find cannot be empty");
        }
    }

    // Marking tasks
    private void mark(String input) throws SharvaException {
        validateMarkInput(input);
        String[] strs = input.split(" ");
        validateIndexCommandLength(strs);
        int taskNumber = extractAndValidateTaskNumber(strs[1]);
        tasks.mark(taskNumber - 1);
    }

    private void validateMarkInput(String input) throws InvalidArgumentsException {
        if (input.trim().equals("mark")) {
            throw new InvalidArgumentsException("Which task must I mark?");
        }
    }

    private void validateIndexCommandLength(String[] strs) throws SharvaException {
        if (strs.length < 2) {
            throw new InvalidCommandException();
            // For errors like marks, mark1, markee...
        } else if (strs.length > 2) {
            throw new InvalidIndexException();
        }
    }

    private int extractAndValidateTaskNumber(String numberStr) throws InvalidIndexException {
        if (!numberStr.matches("\\d+")) {
            throw new InvalidIndexException();
        }
        return Integer.parseInt(numberStr);
    }

    // Unmark methods
    private void unmark(String input) throws SharvaException {
        validateUnmarkInput(input);
        String[] strs = input.split(" ");
        validateIndexCommandLength(strs);
        int taskNumber = extractAndValidateTaskNumber(strs[1]);
        tasks.unmark(taskNumber - 1);
    }

    private void validateUnmarkInput(String input) throws InvalidArgumentsException {
        if (input.trim().equals("unmark")) {
            throw new InvalidArgumentsException("Which task must I unmark?");
        }
    }

    // Deleting tasks
    private void delete(String input) throws SharvaException {
        validateDeleteInput(input);

        String[] strs = input.split(" ");
        validateIndexCommandLength(strs);

        int taskNumber = extractAndValidateTaskNumber(strs[1]);
        tasks.delete(taskNumber - 1);
    }

    private void validateDeleteInput(String input) throws InvalidArgumentsException {
        if (input.trim().equals("delete")) {
            throw new InvalidArgumentsException("Which task must I delete?");
        }
    }

    private void toDo(String input) throws SharvaException {
        validateToDoInput(input);
        String taskName = input.substring(TODO_PREFIX_LENGTH);
        addTodo(taskName);
    }

    private void validateToDoInput(String input) throws InvalidArgumentsException {
        if (input.trim().equals("todo")) {
            throw new InvalidArgumentsException("The description of a todo cannot be empty");
        }
    }

    private void addTodo(String taskName) {
        Task task = new ToDo(taskName);
        tasks.addTask(task);
    }

    // Deadline methods
    private void deadline(String input) throws SharvaException {
        validateDeadlineInput(input);

        int byIndex = findByIndex(input);
        String taskName = extractDeadlineTaskName(input, byIndex);
        String by = extractByDateTime(input, byIndex);

        LocalDateTime due = parseDateTime(by, true);
        addDeadline(taskName, due);
    }

    private void validateDeadlineInput(String input) throws InvalidArgumentsException {
        if (input.trim().equals("deadline")) {
            throw new InvalidArgumentsException("The description of a deadline cannot be empty");
        }
    }

    private int findByIndex(String input) throws InvalidArgumentsException {
        int byIndex = input.indexOf(BY_DELIMITER);
        if (byIndex == -1) {
            throw new InvalidArgumentsException("When is it due?");
        }
        return byIndex;
    }

    /**
     * Extracts and validates a time string from a substring operation.
     * @param input the full input string
     * @param startIndex the starting index for substring
     * @param endIndex the ending index for substring (-1 means to end of string)
     * @param errorMessage the error message to throw if time string is empty
     * @return the trimmed time string
     * @throws InvalidArgumentsException if the time string is empty
     */
    private String extractDateTimeString(
            String input, int startIndex, int endIndex, String errorMessage) throws InvalidArgumentsException {
        String timeString = endIndex == -1
                ? input.substring(startIndex).trim()
                : input.substring(startIndex, endIndex).trim();
        if (timeString.isEmpty()) {
            throw new InvalidArgumentsException(errorMessage);
        }
        return timeString;
    }

    private String extractDeadlineTaskName(String input, int byIndex) throws InvalidArgumentsException {
        return extractTaskDescription(input, DEADLINE_PREFIX_LENGTH, byIndex, "What's the task name?");
    }

    private String extractByDateTime(String input, int byIndex) throws InvalidArgumentsException {
        return extractDateTimeString(input, byIndex + BY_DELIMITER_LENGTH, -1, "When is it due?");
    }

    private void addDeadline(String taskName, LocalDateTime by) {
        Task task = new Deadline(taskName, by);
        tasks.addTask(task);
    }

    // Event methods
    private void event(String input) throws SharvaException {
        validateEventInput(input);

        int fromIndex = findFromIndex(input);
        int toIndex = findToIndex(input);
        validateIndexOrder(fromIndex, toIndex);

        String taskName = extractTaskName(input, fromIndex);
        String from = extractFromDateTime(input, fromIndex, toIndex);
        String to = extractToDateTime(input, toIndex);

        LocalDateTime fromDateTime = parseDateTime(from, false);
        LocalDateTime toDateTime = parseDateTime(to, true);
        validateDateTimeOrder(fromDateTime, toDateTime);

        addEvent(taskName, fromDateTime, toDateTime);
    }

    private void validateEventInput(String input) throws InvalidArgumentsException {
        if (input.trim().equals("event")) {
            throw new InvalidArgumentsException("The description of a event cannot be empty");
        }
    }

    private int findFromIndex(String input) throws InvalidArgumentsException {
        int fromIndex = input.indexOf(FROM_DELIMITER);
        if (fromIndex == -1) {
            throw new InvalidArgumentsException("When does the event start?");
        }
        return fromIndex;
    }

    private int findToIndex(String input) throws InvalidArgumentsException {
        int toIndex = input.indexOf(TO_DELIMITER);
        if (toIndex == -1) {
            throw new InvalidArgumentsException("When does the event end?");
        }
        return toIndex;
    }

    private void validateIndexOrder(int fromIndex, int toIndex) throws InvalidArgumentsException {
        if (toIndex < fromIndex) {
            throw new InvalidArgumentsException("To is before from, that doesn't make sense...");
        }
    }

    private String extractTaskName(String input, int fromIndex) throws InvalidArgumentsException {
        return extractTaskDescription(input, EVENT_PREFIX_LENGTH, fromIndex, "What's the event name?");
    }

    private String extractFromDateTime(String input, int fromIndex, int toIndex) throws InvalidArgumentsException {
        return extractDateTimeString(input, fromIndex + FROM_DELIMITER_LENGTH, toIndex, "When does the event start?");
    }

    private String extractToDateTime(String input, int toIndex) throws InvalidArgumentsException {
        return extractDateTimeString(input, toIndex + TO_DELIMITER_LENGTH, -1, "When does the event end?");
    }

    private void validateDateTimeOrder(
            LocalDateTime fromDateTime, LocalDateTime toDateTime) throws InvalidArgumentsException {
        if (toDateTime.isBefore(fromDateTime)) {
            throw new InvalidArgumentsException("Trying time travel? We do no do that here...");
        }
    }

    private void addEvent(String taskName, LocalDateTime from, LocalDateTime to) {
        Task task = new Event(taskName, from, to);
        tasks.addTask(task);
    }
}
