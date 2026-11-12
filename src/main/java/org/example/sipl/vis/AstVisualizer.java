package org.example.sipl.vis;

import org.example.sipl.ast.Aexp;
import org.example.sipl.ast.AstNode;
import org.example.sipl.ast.Bexp;
import org.example.sipl.ast.Stm;

import java.util.ArrayList;
import java.util.List;

public class AstVisualizer {

    private int nodeCounter = 0;
    private final StringBuilder dotBuilder = new StringBuilder();

    /**
     * Генерує граф AST у форматі DOT.
     */
    public String toDot(AstNode root) {
        nodeCounter = 0;
        dotBuilder.setLength(0);
        dotBuilder.append("digraph AST {\n");
        dotBuilder.append("  node [shape=box, style=filled, fillcolor=\"#E3F2FD\", fontname=\"Helvetica\"];\n");
        traverseDot(root, -1);
        dotBuilder.append("}\n");
        return dotBuilder.toString();
    }

    private int traverseDot(AstNode node, int parentId) {
        if (node == null) return -1;

        int currentId = nodeCounter++;
        String label = getLabel(node);

        dotBuilder.append(String.format("  node%d [label=\"%s\"];\n", currentId, label));

        if (parentId != -1) {
            dotBuilder.append(String.format("  node%d -> node%d;\n", parentId, currentId));
        }

        for (AstNode child : getChildren(node)) {
            traverseDot(child, currentId);
        }
        return currentId;
    }

    // Визначаємо текст для кожного вузла
    private String getLabel(AstNode node) {
        return switch (node) {
            case Stm.Assign a -> "Assign := \\n(" + a.varName + ")";
            case Stm.Skip s -> "Skip";
            case Stm.Block b -> "Block";
            case Stm.If i -> "If";
            case Stm.While w -> "While";
            case Stm.Comp c -> "Comp (;)";
            case Aexp.Num n -> "Num: " + n.value;
            case Aexp.Var v -> "Var: " + v.name;
            case Aexp.BinOp b -> "BinOp: " + b.op;
            case Bexp.RelOp r -> "RelOp: " + r.op;
            case Bexp.Not n -> "Not";
            case Bexp.Or o -> "Or";
            default -> "Unknown Node";
        };
    }

    // Отримуємо дочірні вузли для рекурсивного обходу
    private List<AstNode> getChildren(AstNode node) {
        return switch (node) {
            case Stm.Assign a -> List.of(a.expr);
            case Stm.Skip s -> List.of();
            case Stm.Block b -> List.of(b.body);
            case Stm.If i -> List.of(i.condition, i.thenBranch, i.elseBranch);
            case Stm.While w -> List.of(w.condition, w.body);
            case Stm.Comp c -> List.of(c.stm1, c.stm2);
            case Aexp.Num n -> List.of();
            case Aexp.Var v -> List.of();
            case Aexp.BinOp b -> List.of(b.left, b.right);
            case Bexp.RelOp r -> List.of(r.left, r.right);
            case Bexp.Not n -> List.of(n.expr);
            case Bexp.Or o -> List.of(o.left, o.right);
            default -> List.of();
        };
    }

    /**
     * DTO для JSON серіалізації AST.
     */
    public record AstNodeDto(String type, String label, List<AstNodeDto> children) {}

    public AstNodeDto toJsonTree(AstNode node) {
        if (node == null) return null;

        List<AstNodeDto> children = new ArrayList<>();
        for (AstNode child : getChildren(node)) {
            children.add(toJsonTree(child));
        }

        return new AstNodeDto(
                node.getClass().getSimpleName(),
                getLabel(node).replace("\\n", " "),
                children.isEmpty() ? null : children
        );
    }
}
