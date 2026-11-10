package org.example.sipl.runtime;

import org.example.sipl.ast.Aexp;
import org.example.sipl.ast.Bexp;
import org.example.sipl.ast.Stm;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    // зберігає поточні значення всіх змінних
    private final Map<String, Integer> memory = new HashMap<>();

    public Map<String, Integer> getMemory() {
        return memory;
    }

    /**
     * Обчислює арифметичні вирази.
     */
    public int evalAexp(Aexp expr) {
        return switch (expr) {
            case Aexp.Num num -> num.value;
            case Aexp.Var var -> memory.getOrDefault(var.name, 0);
            case Aexp.BinOp bin -> {
                int left = evalAexp(bin.left);
                int right = evalAexp(bin.right);
                yield switch (bin.op) {
                    case "+" -> left + right;
                    case "-" -> left - right;
                    case "*" -> left * right;
                    default -> throw new IllegalArgumentException("Невідомий оператор: " + bin.op);
                };
            }
            default -> throw new IllegalStateException("Невідомий вузол Aexp: " + expr);
        };
    }

    /**
     * Обчислює логічні вирази.
     */
    public boolean evalBexp(Bexp expr) {
        return switch (expr) {
            case Bexp.RelOp rel -> {
                int left = evalAexp(rel.left);
                int right = evalAexp(rel.right);
                yield switch (rel.op) {
                    case "=" -> left == right;
                    case ">" -> left > right;
                    default -> throw new IllegalArgumentException("Невідомий оператор: " + rel.op);
                };
            }
            case Bexp.Not not -> !evalBexp(not.expr);
            case Bexp.Or or -> evalBexp(or.left) || evalBexp(or.right);
            default -> throw new IllegalStateException("Невідомий вузол Bexp: " + expr);
        };
    }

    /**
     * Виконує команди (оператори) програми.
     */
    public void execute(Stm stmt) {
        switch (stmt) {
            case Stm.Assign assign -> {
                int value = evalAexp(assign.expr);
                memory.put(assign.varName, value);
            }
            case Stm.Skip skip -> {
                // skip
            }
            case Stm.Block block -> {
                execute(block.body);
            }
            case Stm.Comp comp -> {
                execute(comp.stm1);
                execute(comp.stm2);
            }
            case Stm.If ifStm -> {
                if (evalBexp(ifStm.condition)) {
                    execute(ifStm.thenBranch);
                } else {
                    execute(ifStm.elseBranch);
                }
            }
            case Stm.While whileStm -> {
                while (evalBexp(whileStm.condition)) {
                    execute(whileStm.body);
                }
            }
            default -> throw new IllegalStateException("Невідомий вузол Stm: " + stmt);
        }
    }
}
