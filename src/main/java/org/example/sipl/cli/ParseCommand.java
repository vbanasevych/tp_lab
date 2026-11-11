package org.example.sipl.cli;

import org.example.sipl.ast.AstNode;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;

@Command(name = "parse", description = "Перевірити файл та побудувати AST")
public class ParseCommand implements Runnable {

    @Parameters(index = "0", description = "Шлях до файлу SIPL")
    private File file;

    @Override
    public void run() {
        try {
            AstNode ast = SiplParserUtils.parseFile(file);
            System.out.println("Успішний синтаксичний розбір: " + file.getName());
            System.out.println("Кореневий вузол AST: " + ast.getClass().getSimpleName() + " " + ast.getSpan());
        } catch (Exception e) {
            System.err.println("Помилка розбору файлу: " + e.getMessage());
        }
    }
}
