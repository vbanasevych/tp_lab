package org.example.sipl.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "sipl",
        mixinStandardHelpOptions = true,
        version = "0.3.0",
        description = "SIPL Language Toolkit CLI",
        subcommands = {
                ParseCommand.class,
                CheckCommand.class,
                RunCommand.class,
                TraceCommand.class,
                VisParseCommand.class,
                VisAstCommand.class,
                CommandLine.HelpCommand.class
        }
)
public class SiplCli implements Runnable {

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SiplCli()).execute(args);
        System.exit(exitCode);
    }
}
