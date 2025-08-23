import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        File sharva = new File(filePath);
        sharva.getParentFile().mkdirs();

        if (!sharva.exists()) {
            try {
                sharva.createNewFile();
            } catch (IOException e) {
                System.out.println("error in creating file");
                return new ArrayList<Task>();
            }
        }

        try (Scanner scanner = new Scanner(sharva)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" @@@ ");

                try {
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
                        task = new Deadline(parts[2], Sharva.parseDateTime(parts[3], true));
                    } else if (parts[0].equals("E")) {
                        if (parts.length != 5) {
                            throw new InvalidArgumentsException("Skipping event task (invalid format)");
                        }
                        task = new Event(parts[2], Sharva.parseDateTime(parts[3], false), Sharva.parseDateTime(parts[4], true));
                    } else {
                        throw new InvalidArgumentsException("Skipping task (invalid task type)");
                    }

                    if (parts[1].equals("1")) {
                        try {
                            task.markAsDone();
                        } catch (SharvaException e) {
                            System.out.println("marking a marked task, by right this shldnt happen");
                        }
                    } else if (!parts[1].equals("0")) {
                        throw new InvalidArgumentsException("Skipping task (invalid task status)");
                    }
                    tasks.add(task);
                } catch (SharvaException ie) {
                    System.out.println(Sharva.horizontalLine);
                    System.out.println("    " + ie.getMessage());
                    System.out.println(Sharva.horizontalLine);
                }
            }
        } catch (IOException e) {
            System.out.println("problem!");
        }
        return tasks;
    }

    public void save(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.toSaveString()).append("\n");
        }
        String allTasks = sb.toString();
        saveTasks("./data/sharva.txt", allTasks);
    }

    private void saveTasks(String filePath, String allTasks) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, false);
            fileWriter.write(allTasks);
        } catch (IOException e) {
            System.out.println("open error");
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("close error");
                }
            }
        }
    }
}
