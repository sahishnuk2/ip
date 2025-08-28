package sharva.storage;

import sharva.tasks.Deadline;
import sharva.tasks.Event;
import sharva.tasks.Task;
import sharva.tasks.ToDo;
import sharva.exceptions.InvalidArgumentsException;
import sharva.exceptions.SharvaException;
import sharva.exceptions.StorageException;
import sharva.parser.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public LoadResult load() throws SharvaException {
        List<Task> tasks = new ArrayList<>();
        List<String> corruptedTaskErrors = new ArrayList<>();
        File sharva = new File(filePath);
        sharva.getParentFile().mkdirs();

        if (!sharva.exists()) {
            try {
                sharva.createNewFile();
            } catch (IOException e) {
                throw new StorageException("Error creating file: " + filePath);
            }
        }

        try (Scanner scanner = new Scanner(sharva)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" @@@ ");
                try {
                    Task task = parseTaskFromParts(parts);
                    tasks.add(task);
                } catch (SharvaException e) {
                    corruptedTaskErrors.add(e.getMessage());
                }

            }
        } catch (IOException e) {
            throw new StorageException("Error loading tasks in file: " + filePath);
        }

        return new LoadResult(tasks, corruptedTaskErrors);
    }

    public void save(List<Task> tasks) throws SharvaException {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.toSaveString()).append("\n");
        }
        String allTasks = sb.toString();
        saveTasks(filePath, allTasks);
    }

    private void saveTasks(String filePath, String allTasks) throws SharvaException {
        try (FileWriter fileWriter = new FileWriter(filePath, false)) {
            fileWriter.write(allTasks);
        } catch (IOException e) {
            throw new StorageException("Error in saving tasks in file: " + filePath);
        }
    }

    private Task parseTaskFromParts(String[] parts) throws SharvaException {
        Task task;
        if (parts[0].equals("T")) {
            if (parts.length != 3) {
                throw new InvalidArgumentsException("Skipping todo task (invalid format)");
            }
            task = new ToDo(parts[2]);
        } else if (parts[0].equals("D")) {
            if (parts.length != 4) {
                throw new InvalidArgumentsException("Skipping deadline task (invalid format)");
            }
            task = new Deadline(parts[2], Parser.parseDateTime(parts[3], true));
        } else if (parts[0].equals("E")) {
            if (parts.length != 5) {
                throw new InvalidArgumentsException("Skipping event task (invalid format)");
            }
            LocalDateTime from = Parser.parseDateTime(parts[3], false);
            LocalDateTime to = Parser.parseDateTime(parts[4], true);
            if (to.isBefore(from)) {
                throw new InvalidArgumentsException("Skipping task (invalid duration)");
            }
            task = new Event(parts[2], Parser.parseDateTime(parts[3], false), Parser.parseDateTime(parts[4], true));
        } else {
            throw new InvalidArgumentsException("Skipping task (invalid task type)");
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        } else if (!parts[1].equals("0")) {
            throw new InvalidArgumentsException("Skipping task (invalid task status)");
        }
        return task;
    }

    // Inner class to encapsulate result
    public static class LoadResult {
        public final List<Task> tasks;
        public String error = null;

        public LoadResult(List<Task> tasks, List<String> corruptedLineErrors) {
            this.tasks = tasks;
            if (!corruptedLineErrors.isEmpty()) {
                StringBuilder errors = new StringBuilder();
                for (String str : corruptedLineErrors) {
                    errors.append("\n").append("    ").append(str);
                }
                this.error = errors.toString();
            }
        }

        public boolean hasCorruptedLines() {
            return error != null;
        }
    }
}
