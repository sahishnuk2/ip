package sharva.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sharva.exceptions.SharvaException;
import sharva.message.Message;
import sharva.tasks.Deadline;
import sharva.tasks.Event;
import sharva.tasks.Task;
import sharva.tasks.ToDo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    private File tempFile;
    private Storage storage;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("storage-test", ".txt");
        tempFile.deleteOnExit();
        storage = new Storage(tempFile.getPath());
    }

    @AfterEach
    void tearDown() {
        tempFile.delete(); // remove temp file after test
    }

    @Test
    public void load_emptyfile_returnsEmptyList() throws SharvaException {
        Storage.LoadResult result = storage.load();
        List<Task> tasks = result.tasks;
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void load_existingTasks_returnsCorrectList() throws SharvaException, IOException{
        List<String> lines = List.of(
                "T @@@ 0 @@@ task todo",
                "D @@@ 1 @@@ task deadline @@@ 12/12/2025 0700"
        );
        Files.write(tempFile.toPath(), lines);

        Storage.LoadResult result = storage.load();
        List<Task> tasks = result.tasks;

        assertNotNull(tasks);
        assertEquals(2, tasks.size());

        Task todo = tasks.get(0);
        assertInstanceOf(ToDo.class, todo);
        assertEquals("[T][ ] task todo", todo.toString());

        Task deadline = tasks.get(1);
        assertInstanceOf(Deadline.class, deadline);
        assertEquals("[D][X] task deadline (by: 12/12/2025 07:00)", deadline.toString());
    }

    @Test
    public void load_invalidTasks_returnsValidTasksAndErrors() throws IOException, SharvaException {
        List<String> lines = List.of(
                "T @@@ 0 @@@ task todo",
                "A @@@ 0 @@@ task todo", // Wrong TaskType
                "T @@@ 5 @@@ task todo", // Invalid index
                "D @@@ 1 @@@ task deadline @@@ 12/12/2025 0700",
                "D @@@ 1 @@@ task deadline", // invalid format
                "E @@@ 1 @@@ task event @@@ 12/12/2025 0700 @@@ 13/12/2025 0800",
                "E @@@ 1 @@@ task event2 @@@ 12/12/2025 0700 @@@ 11/12/2025 0700", // to before from
                "E @@@ 1 @@@ task event2 @@@ 11/12/2025 0700" // invalid format
        );
        Files.write(tempFile.toPath(), lines);

        Storage.LoadResult result = storage.load();
        List<Task> tasks = result.tasks;

         String expected = """
                 
                 Skipping task (invalid task type)
                 Skipping task (invalid task status)
                 Skipping deadline task (invalid format)
                 Skipping task (invalid duration)
                 Skipping event task (invalid format)\
                 """;

        assertEquals(3, tasks.size());
        assertEquals(expected, result.error);
    }

    @Test
    public void save_validTasks_fileWrittenCorrectly() throws SharvaException, IOException {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new ToDo("todo"));
        tasks.add(new Deadline("deadline", LocalDateTime.of(2025, 12, 12, 7, 0)));
        tasks.add(new Event("event", LocalDateTime.of(2025, 12, 12, 7, 0), LocalDateTime.of(2025, 12, 13, 7, 0)));

        storage.save(tasks);

        String fileContent = Files.readString(tempFile.toPath());

        StringBuilder expected = new StringBuilder();
        for (Task task : tasks) {
            expected.append(task.toSaveString()).append("\n");
        }

        assertEquals(expected.toString(), fileContent);
    }

    @Test
    public void save_emptyTaskList_noFileContent() throws SharvaException, IOException {
        List<Task> tasks = new ArrayList<>();
        storage.save(tasks);

        String fileContent = Files.readString(tempFile.toPath());

        assertEquals("", fileContent);
    }
}
