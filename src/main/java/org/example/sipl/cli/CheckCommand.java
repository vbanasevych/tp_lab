package org.example.sipl.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;

@Command(name = "check", description = "Валідація синтаксису SIPL файлу без виконання")
public class CheckCommand implements Runnable {

    @Parameters(index = "0", description = "Шлях до файлу SIPL")
    private File file;

    @Override
    public void run() {
        try {
            SiplParserUtils.parseFile(file);
            System.out.println("Синтаксис коректний: " + file.getName());
        } catch (Exception e) {
            System.err.println("Синтаксична помилка в файлі " + file.getName() + ": " + e.getMessage());
        }
    }
}
