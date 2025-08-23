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
    public static final String horizontalLine = "    __________________________________________________________________";
    private static final List<Task> tasks = new ArrayList<>();
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

    public static void sayHello() {
        System.out.println(horizontalLine);
        System.out.println("    Hello! I'm Sharva\n    What can I do for you?");
        System.out.println(horizontalLine);
    }

    // Parser functions
    // Adding tasks
    public static void toDo(String input) throws SharvaException {
        if (input.trim().equals("todo")) {
            // for multiple spaces after todo
            throw new InvalidArgumentsException("The description of a todo cannot be empty");
        }
        String taskName = input.substring(5);
        addTodo(taskName);
    }

    public static void deadline(String input) throws SharvaException {
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

    public static void event(String input) throws SharvaException {
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

    // Marking tasks
    public static void mark(String input) throws SharvaException {
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
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new InvalidIndexException();
        }
        markTask(taskNumber - 1);
    }

    public static void unmark(String input) throws SharvaException {
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
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new InvalidIndexException();
        }
        unmarkTask(taskNumber - 1);
    }

    // Deleting tasks
    public static void delete(String input) throws SharvaException {
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
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new InvalidIndexException();
        }
        deleteTask(taskNumber - 1);
    }

    public static void list() {
        System.out.println(horizontalLine);
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("    " + String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }
        System.out.println(horizontalLine);
    }

    public static void handleInvalidInput(String input) throws SharvaException {
        if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
            throw new InvalidArgumentsException("The description of a " + input + " cannot be empty");
        }
        throw new InvalidCommandException();
    }

    public static void sayBye() {
        System.out.println(horizontalLine);
        System.out.println("    Bye bro! See you later!");
        System.out.println(horizontalLine);
    }

    //Helper methods for marking
    private static void markTask(int index) throws SharvaException {
        tasks.get(index).markAsDone();
        System.out.println(horizontalLine);
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("    " + tasks.get(index));
        System.out.println(horizontalLine);
    }

    private static void unmarkTask(int index) throws SharvaException {
        tasks.get(index).undoTask();
        System.out.println(horizontalLine);
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("    " + tasks.get(index));
        System.out.println(horizontalLine);
    }

    // Helper methods to add tasks
    private static void addTodo(String taskName) {
        System.out.println(horizontalLine);
        Task task = new ToDo(taskName);
        tasks.add(task);
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", tasks.size()));
        System.out.println(horizontalLine);
    }

    private static void addDeadline(String taskName, LocalDateTime by) {
        System.out.println(horizontalLine);
        Task task = new Deadline(taskName, by);
        tasks.add(task);
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", tasks.size()));
        System.out.println(horizontalLine);
    }

    private static void addEvent(String taskName, LocalDateTime from, LocalDateTime to) {
        System.out.println(horizontalLine);
        Task task = new Event(taskName, from, to);
        tasks.add(task);
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", tasks.size()));
        System.out.println(horizontalLine);
    }

    // Helper method to delete tasks
    private static void deleteTask(int index) {
        Task task = tasks.get(index);
        tasks.remove(index);
        System.out.println(horizontalLine);
        System.out.println("    Noted. I've removed this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", tasks.size()));
        System.out.println(horizontalLine);
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

    public static void main(String[] args) {
        Storage storage = new Storage("./data/sharva.txt");
        tasks.addAll(storage.load());
        sayHello();
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
                storage.save(tasks);
            } catch (SharvaException e) {
                System.out.println(horizontalLine);
                System.out.println("    " + e.getMessage());
                System.out.println(horizontalLine);
            }
            curr = scanner.nextLine();
        }
        sayBye();
        scanner.close();
    }
}
