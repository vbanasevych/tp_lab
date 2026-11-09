package org.example.sipl.ast;

public abstract class AstNode {
    private SourceSpan span;

    public SourceSpan getSpan() { return span; }
    public void setSpan(SourceSpan span) { this.span = span; }
}
