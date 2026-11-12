package org.example.sipl.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.sipl.ast.AstNode;
import org.example.sipl.ast.Stm;
import org.example.sipl.runtime.ExecutionListener;
import org.example.sipl.runtime.Interpreter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Command(name = "trace", description = "Виконати програму SIPL з покроковим трасуванням станів у форматі JSON")
public class TraceCommand implements Runnable {

    @Parameters(index = "0", description = "Шлях до файлу SIPL")
    private File file;

    @Override
    public void run() {
        try {
            AstNode ast = SiplParserUtils.parseFile(file);
            if (!(ast instanceof Stm stm)) {
                System.err.println("Некоректний кореневий вузол програми.");
                return;
            }

            List<TraceStep> trace = new ArrayList<>();
            Interpreter interpreter = new Interpreter();

            interpreter.addListener(new ExecutionListener() {
                private int stepCounter = 1;
                private Map<String, Integer> beforeMemory;

                @Override
                public void onBeforeStatement(Stm s, Map<String, Integer> currentMemory) {
                    this.beforeMemory = currentMemory;
                }

                @Override
                public void onAfterStatement(Stm s, Map<String, Integer> currentMemory) {
                    String span = s.getSpan() != null ? s.getSpan().toString() : "[?:?]";
                    trace.add(new TraceStep(
                            stepCounter++,
                            s.getClass().getSimpleName(),
                            span,
                            beforeMemory,
                            currentMemory
                    ));
                }
            });

            interpreter.execute(stm);

            // Форматування та вивід у JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            String jsonOutput = mapper.writeValueAsString(trace);
            System.out.println(jsonOutput);

        } catch (Exception e) {
            System.err.println("Помилка під час трасування: " + e.getMessage());
        }
    }
}
