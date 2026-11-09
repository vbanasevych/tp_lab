package org.example.sipl;

import org.antlr.v4.runtime.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SIPLParserTest {

    /**
     * Налаштовує парсер так, щоб він кидав виключення при будь-якій синтаксичній помилці
     */
    private SIPLParser setupParser(String code) {
        SIPLLexer lexer = new SIPLLexer(CharStreams.fromString(code));
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

        SIPLParser parser = new SIPLParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        return parser;
    }

    @ParameterizedTest(name = "[{index}] Позитивний тест: {0}")
    @ValueSource(strings = {
            "begin skip end",
            "begin x := 5 end",
            "begin y := -42 end",
            "begin z := x + y * 2 end",
            "begin res := (a + b) * (c - d) end",
            "begin x := 1; y := 2 end",
            "begin begin skip end end",
            "begin if x > 0 then skip else skip end",
            "begin if not (x = y) then x := 1 else y := 1 end",
            "begin while count > 0 do skip end",
            "begin while x > 0 do begin x := x - 1; y := y * 2 end end",
            "begin if a > b or c = d then a := 0 else b := 0 end"
    })
    void shouldParseValidSIPLPrograms(String program) {
        // Перевіряємо, що виклик .program() не викидає виключень
        assertDoesNotThrow(() -> setupParser(program).program());
    }

    @ParameterizedTest(name = "[{index}] Негативний тест: {0}")
    @ValueSource(strings = {
            "x := 5",                                   // Немає begin/end
            "begin x = 5 end",                          // Неправильний оператор присвоювання (= замість :=)
            "begin 5 := x end",                         // Присвоювання константі
            "begin if x > 0 skip else skip end",        // Пропущено then
            "begin while x > 0 skip end",               // Пропущено do
            "begin x := 2 + * 3 end",                   // Два оператори підряд
            "begin x := (2 + 3 end",                    // Не закрита дужка
            "begin x := 1 y := 2 end",                  // Пропущено ;
            "begin iff x > 0 then skip else skip end",  // Одруківка в ключовому слові (iff)
            "begin x := 1; ; y := 2 end",               // Подвійна крапка з комою (пустий оператор не підтримується)
            "begin skip; end"                           // Зайва крапка з комою перед end (бо ; об'єднує два оператори)
    })
    void shouldFailOnInvalidSIPLPrograms(String program) {
        // Перевіряємо, що парсер викидає RuntimeException на синтаксичну помилку
        assertThrows(RuntimeException.class, () -> setupParser(program).program());
    }
}

/**
 * Лісенер, який перехоплює помилки ANTLR і перетворює їх на RuntimeException.
 */
class ThrowingErrorListener extends BaseErrorListener {
    public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new RuntimeException("Syntax error at line " + line + ":" + charPositionInLine + " " + msg);
    }
}
