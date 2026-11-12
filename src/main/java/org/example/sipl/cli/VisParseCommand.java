package org.example.sipl.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.sipl.SIPLParser;
import org.example.sipl.vis.ParseTreeVisualizer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Command(name = "vis-parse", description = "Генерує візуалізацію Parse Tree у форматах DOT та JSON")
public class VisParseCommand implements Runnable {

    @Parameters(index = "0", description = "Шлях до файлу SIPL")
    private File file;

    @Option(names = {"-f", "--format"}, description = "Формат виводу: dot, json або both", defaultValue = "both")
    private String format;

    @Override
    public void run() {
        try {
            SIPLParser parser = SiplParserUtils.createParser(file);
            ParseTree tree = parser.program();
            ParseTreeVisualizer visualizer = new ParseTreeVisualizer();

            String baseName = file.getName().replaceFirst("[.][^.]+$", "");

            if (format.equals("dot") || format.equals("both")) {
                String dotOutput = visualizer.toDot(tree, parser);
                Path dotPath = Path.of(baseName + "_parsetree.dot");
                Files.writeString(dotPath, dotOutput);
                System.out.println("DOT файл згенеровано: " + dotPath.toAbsolutePath());
            }

            if (format.equals("json") || format.equals("both")) {
                var jsonDto = visualizer.toJsonTree(tree, parser);
                ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
                Path jsonPath = Path.of(baseName + "_parsetree.json");
                mapper.writeValue(jsonPath.toFile(), jsonDto);
                System.out.println("JSON файл згенеровано: " + jsonPath.toAbsolutePath());
            }

        } catch (Exception e) {
            System.err.println("Помилка під час візуалізації: " + e.getMessage());
        }
    }
}
