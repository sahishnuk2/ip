import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sharva {
    private Storage storage;
    private TaskList tasks;
    private Message message;
    public static final String horizontalLine = "    __________________________________________________________________";
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

    public Sharva(String filePath) {
        this.storage = new Storage(filePath);
        this.message = new Message();
        this.tasks = new TaskList(storage.load(), message);
    }

    // Parser functions
    // Adding tasks
    public void toDo(String input) throws SharvaException {
        if (input.trim().equals("todo")) {
            // for multiple spaces after todo
            throw new InvalidArgumentsException("The description of a todo cannot be empty");
        }
        String taskName = input.substring(5);
        addTodo(taskName);
    }

    public void deadline(String input) throws SharvaException {
        if (input.trim().equals("deadline")) {
            // for multiple spaces after deadline
            throw new InvalidArgumentsException("The description of a deadline cannot be empty");
        }
        int byIndex = input.indexOf(" /by ");
        if (byIndex == -1) {
            throw new InvalidArgumentsException("When is it due?");
        }
        String taskName = input.substring(8, byIndex).trim();
        if (taskName.isEmpty()) {
            throw new InvalidArgumentsException("What's the task name?");
        }
        String by = input.substring(byIndex + 5).trim();
        if (by.isEmpty()) {
            throw new InvalidArgumentsException("When is it due?");
        }
        LocalDateTime due = parseDateTime(by, true);
        addDeadline(taskName, due);
    }

    public void event(String input) throws SharvaException {
        if (input.trim().equals("event")) {
            // for multiple spaces after event
            throw new InvalidArgumentsException("The description of a event cannot be empty");
        }
        int fromIndex = input.indexOf(" /from ");
        if (fromIndex == -1) {
            throw new InvalidArgumentsException("When does the event start?");
        }
        int toIndex = input.indexOf(" /to ");
        if (toIndex == -1) {
            throw new InvalidArgumentsException("When does the event end?");
        }
        if (toIndex < fromIndex) {
            throw new InvalidArgumentsException("To is before from, that doesn't make sense...");
        }
        String taskName = input.substring(5, fromIndex).trim();
        if (taskName.isEmpty()) {
            throw new InvalidArgumentsException("What's the event name?");
        }
        String from = input.substring(fromIndex + 7, toIndex).trim();
        if (from.isEmpty()) {
            throw new InvalidArgumentsException("When does the event start?");
        }
        LocalDateTime fromDateTime = parseDateTime(from, false);

        String to = input.substring(toIndex + 5).trim();
        if (to.isEmpty()) {
            throw new InvalidArgumentsException("When does the event end?");
        }
        LocalDateTime toDateTime = parseDateTime(to, true);
        if (toDateTime.isBefore(fromDateTime)) {
            throw new InvalidArgumentsException("Trying time travel? We do no do that here...");
        }
        addEvent(taskName, fromDateTime, toDateTime);
    }

    // Helper methods to add tasks
    private void addTodo(String taskName) {
        Task task = new ToDo(taskName);
        tasks.addTask(task);
    }

    private void addDeadline(String taskName, LocalDateTime by) {
        Task task = new Deadline(taskName, by);
        tasks.addTask(task);
    }

    private void addEvent(String taskName, LocalDateTime from, LocalDateTime to) {
        Task task = new Event(taskName, from, to);
        tasks.addTask(task);
    }

    // Marking tasks
    public void mark(String input) throws SharvaException {
        if (input.trim().equals("mark")) {
            throw new InvalidArgumentsException("Which task must I mark?");
        }

        String[] strs = input.split(" ");
        if (strs.length < 2) {
            throw new InvalidCommandException();
            // For errors like marks, mark1, markee...
        } else if (strs.length > 2) {
            throw new InvalidIndexException();
        }

        if (!strs[1].matches("\\d+")) {
            throw new InvalidIndexException();
        }
        int taskNumber = Integer.parseInt(strs[1]);
        tasks.mark(taskNumber - 1);
    }

    public void unmark(String input) throws SharvaException {
        if (input.trim().equals("unmark")) {
            throw new InvalidArgumentsException("Which task must I unmark?");
        }

        String[] strs = input.split(" ");
        if (strs.length < 2) {
            throw new InvalidCommandException();
            // For errors like marks, mark1, markee...
        } else if (strs.length > 2) {
            throw new InvalidIndexException();
        }

        int taskNumber = Integer.parseInt(strs[1]);
        tasks.unmark(taskNumber - 1);
    }

    // Deleting tasks
    public void delete(String input) throws SharvaException {
        if (input.trim().equals("delete")) {
            throw new InvalidArgumentsException("Which task must I delete?");
        }

        String[] strs = input.split(" ");
        if (strs.length < 2) {
            throw new InvalidCommandException();
        } else if (strs.length > 2) {
            throw new InvalidIndexException();
        }

        if (!strs[1].matches("\\d+")) {
            throw new InvalidIndexException();
        }
        int taskNumber = Integer.parseInt(strs[1]);
        tasks.delete(taskNumber - 1);
    }

    public void list() {
        System.out.println(horizontalLine);
        System.out.println(tasks.list());
        System.out.println(horizontalLine);
    }

    public void handleInvalidInput(String input) throws SharvaException {
        if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
            throw new InvalidArgumentsException("The description of a " + input + " cannot be empty");
        }
        throw new InvalidCommandException();
    }


    // Helper method to parse LocalDate, LocalTime
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

    public static LocalDateTime parseDateTime(String input, boolean isEnd) throws SharvaException {
        String[] parts = input.split(" ");
        LocalDate date;
        LocalTime time = null;
        if (parts.length == 1) {
            date = parseDate(parts[0]);
        } else if (parts.length == 2 ) {
            date = parseDate(parts[0]);
            time = parseTime(parts[1]);
        } else if (parts.length == 3) {
            date = parseDate(parts[0]);
            time = parseTime(parts[1] + " " + parts[2]);
        } else {
            throw new InvalidArgumentsException("Invalid date and time format");
        }
        if (time == null && isEnd) {
            return LocalDateTime.of(date, LocalTime.of(23, 59));
        } else if (time == null) {
            return LocalDateTime.of(date, LocalTime.of(0, 0));
        }
        return LocalDateTime.of(date, time);
    }

    public void run() {
        message.sayHello();
        Scanner scanner = new Scanner(System.in);
        String curr = scanner.nextLine();
        while (!curr.equals("bye")) {
            try {
                if (curr.equals("list")) {
                    list();
                } else if (curr.startsWith("mark")) {
                    mark(curr);
                } else if (curr.startsWith("unmark")) {
                    unmark(curr);
                } else if (curr.startsWith("delete")) {
                    delete(curr);
                } else if (curr.startsWith("todo ")) {
                    toDo(curr);
                } else if (curr.startsWith("deadline ")) {
                    deadline(curr);
                } else if (curr.startsWith("event ")){
                    event(curr);
                } else {
                    handleInvalidInput(curr);
                }
                storage.save(tasks.getTasks());
            } catch (SharvaException e) {
                System.out.println(horizontalLine);
                System.out.println("    " + e.getMessage());
                System.out.println(horizontalLine);
            }
            curr = scanner.nextLine();
        }
        message.sayBye();
        scanner.close();
    }

    public static void main(String[] args) {
        Sharva sharva = new Sharva("./data/sharva.txt");
        sharva.run();
    }
}
