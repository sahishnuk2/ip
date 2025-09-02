//package sharva.message;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import sharva.tasks.ToDoStub;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class MessageTest {
//    private Message message;
//    ByteArrayOutputStream outContent;
//    PrintStream originalOut = System.out;
//    ToDoStub task;
//
//    @BeforeEach
//    public void setUp() {
//        this.message = new Message();
//        outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        task = new ToDoStub();
//    }
//
//    @AfterEach
//    public void finish() {
//        System.setOut(originalOut);
//    }
//
//    private String createExpectedMessage(String input) {
//        return Message.HORIZONTAL_LINE + "\n" +
//                input + "\n" +
//                Message.HORIZONTAL_LINE + "\n";
//
//    }
//
//    @Test
//    public void sayHello_default_success() {
//        message.sayHello();
//        String expected = createExpectedMessage("    Hello! I'm Sharva\n    What can I do for you?");
//        assertEquals(expected, outContent.toString());
//    }
//
//    @Test
//    public void sayBye_default_success() {
//        message.sayBye();
//        String expected = createExpectedMessage("    Bye bro! See you later!");
//        assertEquals(expected, outContent.toString());
//    }
//
//    @Test
//    public void mark_default_success()  {
//        task.markAsDone();
//        message.mark(task);
//        String expected = createExpectedMessage("    Nice! I've marked this task as done:\n" + "    " + task);
//        assertEquals(expected, outContent.toString());
//    }
//
//    @Test
//    public void unmark_default_success()   {
//        task.undoTask();
//        message.unmark(task);
//        String expected = createExpectedMessage("    OK, I've marked this task as not done yet:\n" + "    " + task);
//        assertEquals(expected, outContent.toString());
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {1, 2, 3})
//    public void add_default_success(int index) {
//        outContent.reset();
//        message.addTask(task, index);
//        String expected = createExpectedMessage(
//                "    Got it. I've added this task:\n"+
//                        "    " + task + "\n" +
//                        String.format("    Now You have %d task(s) in the list", index)
//        );
//        assertEquals(expected, outContent.toString());
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {1, 2, 3})
//    public void delete_default_success(int index) {
//        outContent.reset();
//        message.deleteTask(task, index);
//        String expected = createExpectedMessage(
//                "    Noted. I've removed this task:\n"+
//                        "    " + task + "\n" +
//                        String.format("    Now You have %d task(s) in the list", index)
//        );
//        assertEquals(expected, outContent.toString());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"hi", "bye", "you are done with all your tasks",  "error message (good error)"})
//    public void echo_default_success(String str) {
//        outContent.reset();
//        message.echo(str);
//        String expected = createExpectedMessage(str);
//        assertEquals(expected, outContent.toString());
//    }
//}
