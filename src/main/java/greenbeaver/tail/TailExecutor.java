package greenbeaver.tail;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.jetbrains.annotations.Nullable;

class TailExecutor {

    private static final String lineSeparator = System.getProperty("line.separator");
    private static final String END_OF_CONSOLE_INPUT_MARKER = "*END_OF_INPUT*";

    private ArrayList<List<String>> texts;
    {
        texts = new ArrayList<>();
    }

    private void getTexts(@Nullable ArrayList<String> inputFileName) throws IOException {
        if (inputFileName != null) {
            boolean addFileName = inputFileName.size() > 1;
            for (String fileName: inputFileName) {
                List<String> text = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
                if (addFileName) {
                    text.add(0, fileName);
                }
                texts.add(text);
            }
        } else {
            Scanner in = new Scanner(System.in);
            ArrayList<String> text = new ArrayList<>();
            System.out.println("Write " + END_OF_CONSOLE_INPUT_MARKER + " to mark the end of your input.");
            String line = in.nextLine();
            while (!line.equals(END_OF_CONSOLE_INPUT_MARKER)) {
                text.add(line);
                line = in.nextLine();
            }
            texts.add(text);
            in.close();
        }
    }

    private ArrayList<ArrayList<String>> extractLines(int linesAmount) {
        ArrayList<ArrayList<String>> tails = new ArrayList<>();
        int linesAmountReduction = texts.size() > 1 ? -1 : 0;
        boolean startsWithFileName = texts.size() > 1;
        for (List<String> oneFileText: texts) {
            if (linesAmount > oneFileText.size() + linesAmountReduction) {
                throw new IllegalArgumentException("There are only "
                        + (oneFileText.size() + linesAmountReduction)
                        + " lines in " + oneFileText.get(0));
            }
            ArrayList<String> tail = new ArrayList<>(oneFileText.subList(oneFileText.size()
                    - linesAmount, oneFileText.size()));
            if (startsWithFileName) {
                tail.add(0, oneFileText.get(0));
            }
            tails.add(tail);
        }
        return tails;
    }

    private ArrayList<String> extractSymbols(int symbolsAmount) {
        ArrayList<String> tails = new ArrayList<>();
        boolean startsWithFileName = texts.size() > 1;
        for (List<String> oneFileText: texts) {
            StringBuilder oneFileTextBuilt = new StringBuilder();
            for (int i = startsWithFileName ? 1 : 0; i <oneFileText.size(); i++) {
                oneFileTextBuilt.append(oneFileText.get(i));
            }
            if (symbolsAmount > oneFileTextBuilt.length()) {
                throw new IllegalArgumentException("There are only "
                        + oneFileTextBuilt.length() + " symbols in " + oneFileText.get(0));
            }
            tails.add((startsWithFileName ? (oneFileText.get(0) + lineSeparator) : "")
                    + oneFileTextBuilt.substring(oneFileTextBuilt.length() - symbolsAmount, oneFileTextBuilt.length()));
        }
        return tails;
    }

    private void linesOut(@Nullable String outputFileName, ArrayList<ArrayList<String>> output) throws IOException {
        if (outputFileName == null) {
            for (ArrayList<String> tailOfOneFile: output) {
                for (String line: tailOfOneFile) {
                    System.out.println(line);
                }
            }
        } else {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName));
            for (ArrayList<String> tailOfOneFile: output) {
                for (String line: tailOfOneFile) {
                    bufferedWriter.write(line + lineSeparator);
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    private void symbolsOut(@Nullable String outputFileName, ArrayList<String> output) throws IOException {
        if (outputFileName == null) {
            for (String tailOfOneFile: output) {
                System.out.println(tailOfOneFile + lineSeparator);
            }
        } else {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName));
            for (String tailOfOneFile: output) {
                bufferedWriter.write(tailOfOneFile + lineSeparator);
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    void execute(@Nullable ArrayList<String> inputFileName,
                        @Nullable String outputFileName,
                        @Nullable String symbolsAmount,
                        @Nullable String linesAmount) throws IOException {
        getTexts(inputFileName);
        if (symbolsAmount != null && linesAmount != null) {
            throw new IllegalArgumentException("You can only set one of the -c and -n flags");
        }
        if (symbolsAmount == null && linesAmount == null) {
            linesOut(outputFileName, extractLines(10));
        }
        if (linesAmount != null) {
            linesOut(outputFileName, extractLines(Integer.parseInt(linesAmount)));
        }
        if (symbolsAmount != null) {
            symbolsOut(outputFileName, extractSymbols(Integer.parseInt(symbolsAmount)));
        }
    }
}
