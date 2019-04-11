package greenbeaver.tail;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class test {

    private static final String lineSeparator = System.getProperty("line.separator");

    private void assertFileContent(String expectedContent, String fileName) throws IOException {
        List<String> actualContentList = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        StringBuilder actualContent = new StringBuilder();
        for (String line: actualContentList) {
            actualContent.append(line).append(lineSeparator);
        }
        assertEquals(expectedContent, actualContent.toString());
    }

    @Test
    void test() throws IOException {
        ArrayList<String> incorrectFileNames =
                new ArrayList<>(Arrays.asList("E:\\MyProjects\\Jabka\\tail\\input\\input1.txt", "E:\\no.txt"));
        assertThrows(NoSuchFileException.class, () -> new TailExecutor().execute(incorrectFileNames,
                null,
                null,
                null));
        new TailExecutor().execute(
                new ArrayList<>(Collections.singletonList("E:\\MyProjects\\Jabka\\tail\\input\\input2.txt")),
                "E:\\MyProjects\\Jabka\\tail\\output\\out1.txt",
                null,
                "4");
        assertFileContent("All the roses in the garden -" +
                lineSeparator +
                "They bow an ask her pardon," +
                lineSeparator +
                "For no one could match the beauty" +
                lineSeparator +
                "Of the Queen of all Argyll!" +
                lineSeparator, "E:\\MyProjects\\Jabka\\tail\\output\\out1.txt");
        new TailExecutor().execute(
                new ArrayList<>(Arrays.asList("E:\\MyProjects\\Jabka\\tail\\input\\input2.txt",
                        "E:\\MyProjects\\Jabka\\tail\\input\\input3.txt")),
                "E:\\MyProjects\\Jabka\\tail\\output\\out1.txt",
                "5",
                null);
        assertFileContent("E:\\MyProjects\\Jabka\\tail\\input\\input2.txt" +
                lineSeparator +
                "gyll!" +
                lineSeparator +
                "E:\\MyProjects\\Jabka\\tail\\input\\input3.txt" +
                lineSeparator +
                " line" +
                lineSeparator, "E:\\MyProjects\\Jabka\\tail\\output\\out1.txt");
        assertThrows(IllegalArgumentException.class, () -> new TailExecutor().execute(
                new ArrayList<>(Arrays.asList("E:\\MyProjects\\Jabka\\tail\\input\\input2.txt",
                        "E:\\MyProjects\\Jabka\\tail\\input\\input3.txt")),
                "E:\\MyProjects\\Jabka\\tail\\output\\out1.txt",
                "5",
                "7"
        ));
        assertThrows(IllegalArgumentException.class, () -> new TailExecutor().execute(
                new ArrayList<>(Collections.singletonList("E:\\MyProjects\\Jabka\\tail\\input\\input1.txt")),
                null,
                null,
                "17"));
        assertThrows(IllegalArgumentException.class, () -> new TailExecutor().execute(
                new ArrayList<>(Collections.singletonList("E:\\MyProjects\\Jabka\\tail\\input\\input3.txt")),
                null,
                "14",
                null));
        new TailExecutor().execute(
                new ArrayList<>(Arrays.asList("E:\\MyProjects\\Jabka\\tail\\input\\input2.txt",
                        "E:\\MyProjects\\Jabka\\tail\\input\\input1.txt")),
                "E:\\MyProjects\\Jabka\\tail\\output\\out2.txt",
                null,
                null);
        assertFileContent("E:\\MyProjects\\Jabka\\tail\\input\\input2.txt" +
                        lineSeparator +
                        "No word can paint the picture" +
                        lineSeparator +
                        "Of the Queen of all Argyll!" +
                        lineSeparator +
                        "***" +
                        lineSeparator +
                        "And if you could have seen of her," +
                        lineSeparator +
                        "Boys, if you could just been there!" +
                        lineSeparator +
                        "The swan was in her movement and the morning in her smile." +
                        lineSeparator +
                        "All the roses in the garden -" +
                        lineSeparator +
                        "They bow an ask her pardon," +
                        lineSeparator +
                        "For no one could match the beauty" +
                        lineSeparator +
                        "Of the Queen of all Argyll!" +
                        lineSeparator +
                        "E:\\MyProjects\\Jabka\\tail\\input\\input1.txt" +
                        lineSeparator +
                        "In the land" +
                        lineSeparator +
                        "Of submarines" +
                        lineSeparator +
                        "So we sailed" +
                        lineSeparator +
                        "Onto the sun" +
                        lineSeparator +
                        "Till we found" +
                        lineSeparator +
                        "A sea of green" +
                        lineSeparator +
                        "And we lived" +
                        lineSeparator +
                        "Beneath the waves" +
                        lineSeparator +
                        "In our yellow" +
                        lineSeparator +
                        "Submarine" +
                        lineSeparator,
                "E:\\MyProjects\\Jabka\\tail\\output\\out2.txt");
    }
}
