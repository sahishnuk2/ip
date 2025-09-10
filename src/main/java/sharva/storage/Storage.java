package sharva.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sharva.exceptions.InvalidArgumentsException;
import sharva.exceptions.SharvaException;
import sharva.exceptions.StorageException;
import sharva.parser.Parser;
import sharva.tasks.Deadline;
import sharva.tasks.Event;
import sharva.tasks.Task;
import sharva.tasks.ToDo;

/**
 * Handles storing and retrieving task from a file.
 */
public class Storage {
    private static final String SEPARATOR = " @@@ ";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String DONE_STATUS = "1";
    private static final String NOT_DONE_STATUS = "0";

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file. If certain lines are corrupted,
     * the error messages are recorded.
     * @return a LoadResult object that stores the tasks and errors
     * @throws SharvaException if storage file cannot be created
     */
    public LoadResult load() throws SharvaException {
        List<Task> tasks = new ArrayList<>();
        List<String> corruptedTaskErrors = new ArrayList<>();

        File storageFile = initializeStorageFile();
        loadTasksFromFile(storageFile, tasks, corruptedTaskErrors);

        return new LoadResult(tasks, corruptedTaskErrors);
    }

    /**
     * Saves tasks into the storage file in string format.
     * @param tasks the list of tasks to be saved
     * @throws SharvaException if an I/O error occurs during saving
     */
    public void save(List<Task> tasks) throws SharvaException {
        String allTasks = buildTasksString(tasks);
        writeTasksToFile(allTasks);
    }

    private File initializeStorageFile() throws StorageException {
        File storageFile = new File(filePath);
        createDirectoryIfNeeded(storageFile);
        createFileIfNeeded(storageFile);
        return storageFile;
    }

    private void createDirectoryIfNeeded(File storageFile) {
        storageFile.getParentFile().mkdirs();
    }

    private void createFileIfNeeded(File storageFile) throws StorageException {
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                throw new StorageException("Error creating file: " + filePath);
            }
        }
    }

    private void loadTasksFromFile(File storageFile, List<Task> tasks, List<String> errors) throws StorageException {
        try (Scanner scanner = new Scanner(storageFile)) {
            while (scanner.hasNextLine()) {
                processTaskLine(scanner.nextLine(), tasks, errors);
            }
        } catch (IOException e) {
            throw new StorageException("Error loading tasks in file: " + filePath);
        }
    }

    private void processTaskLine(String line, List<Task> tasks, List<String> errors) {
        try {
            String[] parts = line.split(SEPARATOR);
            Task task = parseTaskFromParts(parts);
            tasks.add(task);
        } catch (SharvaException e) {
            errors.add(e.getMessage());
        }
    }

    private String buildTasksString(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.toSaveString()).append("\n");
        }
        return sb.toString();
    }

    private void writeTasksToFile(String allTasks) throws StorageException {
        try (FileWriter fileWriter = new FileWriter(filePath, false)) {
            fileWriter.write(allTasks);
        } catch (IOException e) {
            throw new StorageException("Error in saving tasks in file: " + filePath);
        }
    }

    private Task parseTaskFromParts(String[] parts) throws SharvaException {
        Task task = createTaskFromParts(parts);
        setTaskStatus(task, parts);
        return task;
    }

    private Task createTaskFromParts(String[] parts) throws SharvaException {
        String taskType = parts[0];

        switch (taskType) {
        case TODO_TYPE:
            return createTodoTask(parts);
        case DEADLINE_TYPE:
            return createDeadlineTask(parts);
        case EVENT_TYPE:
            return createEventTask(parts);
        default:
            throw new InvalidArgumentsException("Skipping task (invalid task type)");
        }
    }

    private Task createTodoTask(String[] parts) throws InvalidArgumentsException {
        validatePartsLength(parts, 3, "todo");
        return new ToDo(parts[2]);
    }

    private Task createDeadlineTask(String[] parts) throws SharvaException {
        validatePartsLength(parts, 4, "deadline");
        LocalDateTime due = Parser.parseDateTime(parts[3], true);
        return new Deadline(parts[2], due);
    }

    private Task createEventTask(String[] parts) throws SharvaException {
        validatePartsLength(parts, 5, "event");
        LocalDateTime from = Parser.parseDateTime(parts[3], false);
        LocalDateTime to = Parser.parseDateTime(parts[4], true);
        validateEventDuration(from, to);
        return new Event(parts[2], from, to);
    }

    private void validatePartsLength(
            String[] parts, int expectedLength, String taskType) throws InvalidArgumentsException {
        if (parts.length != expectedLength) {
            throw new InvalidArgumentsException("Skipping " + taskType + " task (invalid format)");
        }
    }

    private void validateEventDuration(LocalDateTime from, LocalDateTime to) throws InvalidArgumentsException {
        if (to.isBefore(from)) {
            throw new InvalidArgumentsException("Skipping task (invalid duration)");
        }
    }

    private void setTaskStatus(Task task, String[] parts) throws SharvaException {
        String status = parts[1];

        if (DONE_STATUS.equals(status)) {
            task.markAsDone();
        } else if (!NOT_DONE_STATUS.equals(status)) {
            throw new InvalidArgumentsException("Skipping task (invalid task status)");
        }
    }

    // Inner class to encapsulate result
    public static class LoadResult {
        public final List<Task> tasks;
        public String error = null;

        public LoadResult(List<Task> tasks, List<String> corruptedLineErrors) {
            this.tasks = tasks;
            this.error = buildErrorMessage(corruptedLineErrors);
        }

        private String buildErrorMessage(List<String> corruptedLineErrors) {
            if (corruptedLineErrors.isEmpty()) {
                return null;
            }

            StringBuilder errors = new StringBuilder();
            for (String str : corruptedLineErrors) {
                errors.append("\n").append(str);
            }
            return errors.toString();
        }

        public boolean hasCorruptedLines() {
            return error != null;
        }
    }
}
