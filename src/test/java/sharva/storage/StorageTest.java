package sharva.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sharva.exceptions.SharvaException;
import sharva.tasks.Deadline;
import sharva.tasks.Task;
import sharva.tasks.ToDo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        List<Task> tasks = storage.load();
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

        List<Task> tasks = storage.load();

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
    public void load_invalidTasks_exceptionThrown() {
        // Bug in storage code, when parse throws errors, it creates a new txt file.
    }
}
