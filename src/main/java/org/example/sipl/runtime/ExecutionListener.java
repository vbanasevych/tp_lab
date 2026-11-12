package org.example.sipl.runtime;

import org.example.sipl.ast.Stm;
import java.util.Map;

/**
 * Інтерфейс для перехоплення подій виконання програми (Trace).
 */
public interface ExecutionListener {
    /**
     * Викликається перед виконанням поточного оператора.
     * @param stmt оператор, що буде виконано
     * @param currentMemory поточний стан змінних (read-only копія)
     */
    void onBeforeStatement(Stm stmt, Map<String, Integer> currentMemory);

    /**
     * Викликається одразу після виконання оператора.
     */
    void onAfterStatement(Stm stmt, Map<String, Integer> currentMemory);
}
