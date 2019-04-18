package greenbeaver.tail;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.ArrayList;

public class TailLauncher {

    @Argument(usage = "Input file name", metaVar = "fileN", multiValued = true)
    private ArrayList<String> inputFileName;

    @Option(name = "-o", usage = "Output file name", metaVar = "ofile")
    private String outputFileName;

    @Option(name = "-c", usage = "Amount of symbols to extract", metaVar = "num")
    private String symbolsAmount;

    @Option(name = "-n", usage = "Amount of limes to extract", metaVar = "num")
    private String linesAmount;

    public static void main(String[] args) throws IOException {
        new TailLauncher().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }

        TailExecutor tailExecutor = new TailExecutor();
        tailExecutor.execute(inputFileName, outputFileName, symbolsAmount, linesAmount);
    }
}
