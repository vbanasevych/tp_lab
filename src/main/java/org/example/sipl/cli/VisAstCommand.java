package org.example.sipl.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.sipl.ast.AstNode;
import org.example.sipl.vis.AstVisualizer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Command(name = "vis-ast", description = "Генерує візуалізацію абстрактного синтаксичного дерева (AST) у DOT/JSON")
public class VisAstCommand implements Runnable {

    @Parameters(index = "0", description = "Шлях до файлу SIPL")
    private File file;

    @Option(names = {"-f", "--format"}, description = "Формат виводу: dot, json або both", defaultValue = "both")
    private String format;

    @Override
    public void run() {
        try {
            AstNode ast = SiplParserUtils.parseFile(file);
            AstVisualizer visualizer = new AstVisualizer();
            String baseName = file.getName().replaceFirst("[.][^.]+$", "");

            if (format.equals("dot") || format.equals("both")) {
                String dotOutput = visualizer.toDot(ast);
                Path dotPath = Path.of(baseName + "_ast.dot");
                Files.writeString(dotPath, dotOutput);
                System.out.println("DOT файл AST згенеровано: " + dotPath.toAbsolutePath());
            }

            if (format.equals("json") || format.equals("both")) {
                var jsonDto = visualizer.toJsonTree(ast);
                ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
                Path jsonPath = Path.of(baseName + "_ast.json");
                mapper.writeValue(jsonPath.toFile(), jsonDto);
                System.out.println("JSON файл AST згенеровано: " + jsonPath.toAbsolutePath());
            }

        } catch (Exception e) {
            System.err.println("Помилка візуалізації AST: " + e.getMessage());
        }
    }
}
