package org.example.sipl.ast;

public record SourceSpan(int line, int column) {
    @Override
    public String toString() {
        return "[" + line + ":" + column + "]";
    }
}
