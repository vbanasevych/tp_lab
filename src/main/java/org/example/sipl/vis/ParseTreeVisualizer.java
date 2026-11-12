package org.example.sipl.vis;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeVisualizer {

    private int nodeCounter = 0;
    private final StringBuilder dotBuilder = new StringBuilder();

    /**
     * Генерує граф у форматі DOT (для Graphviz).
     */
    public String toDot(ParseTree tree, Parser parser) {
        nodeCounter = 0;
        dotBuilder.setLength(0);
        dotBuilder.append("digraph ParseTree {\n");
        dotBuilder.append("  node [shape=box, fontname=\"Helvetica\"];\n");
        traverseDot(tree, parser, -1);
        dotBuilder.append("}\n");
        return dotBuilder.toString();
    }

    private int traverseDot(ParseTree node, Parser parser, int parentId) {
        int currentId = nodeCounter++;
        String label = Trees.getNodeText(node, parser)
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        dotBuilder.append(String.format("  node%d [label=\"%s\"];\n", currentId, label));

        if (parentId != -1) {
            dotBuilder.append(String.format("  node%d -> node%d;\n", parentId, currentId));
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            traverseDot(node.getChild(i), parser, currentId);
        }
        return currentId;
    }

    /**
     * DTO для JSON серіалізації дерева.
     */
    public record ParseTreeNodeDto(String name, List<ParseTreeNodeDto> children) {}

    /**
     * Перетворює ParseTree у структуру, готову до серіалізації в JSON через Jackson.
     */
    public ParseTreeNodeDto toJsonTree(ParseTree node, Parser parser) {
        String name = Trees.getNodeText(node, parser);
        List<ParseTreeNodeDto> children = new ArrayList<>();
        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(toJsonTree(node.getChild(i), parser));
        }
        return new ParseTreeNodeDto(name, children.isEmpty() ? null : children);
    }
}
