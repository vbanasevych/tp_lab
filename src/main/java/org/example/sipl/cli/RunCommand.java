package org.example.sipl.cli;

import org.example.sipl.ast.AstNode;
import org.example.sipl.ast.Stm;
import org.example.sipl.runtime.Interpreter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;

@Command(name = "run", description = "Виконати програму SIPL та вивести фінальний стан змінних")
public class RunCommand implements Runnable {

    @Parameters(index = "0", description = "Шлях до файлу SIPL")
    private File file;

    @Override
    public void run() {
        try {
            AstNode ast = SiplParserUtils.parseFile(file);
            if (ast instanceof Stm stm) {
                Interpreter interpreter = new Interpreter();
                interpreter.execute(stm);
                System.out.println("Виконання завершено успішно.");
                System.out.println("Фінальний стан пам'яті (змінні): " + interpreter.getMemory());
            } else {
                System.err.println("Некоректний кореневий вузол програми.");
            }
        } catch (Exception e) {
            System.err.println("Помилка під час виконання: " + e.getMessage());
        }
    }
}
