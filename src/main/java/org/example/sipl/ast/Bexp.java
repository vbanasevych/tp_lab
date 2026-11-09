package org.example.sipl.ast;

public abstract class Bexp extends AstNode {

    public static class RelOp extends Bexp {
        public final String op; // "=", ">"
        public final Aexp left;
        public final Aexp right;

        public RelOp(String op, Aexp left, Aexp right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }
    }

    public static class Not extends Bexp {
        public final Bexp expr;
        public Not(Bexp expr) { this.expr = expr; }
    }

    public static class Or extends Bexp {
        public final Bexp left;
        public final Bexp right;

        public Or(Bexp left, Bexp right) {
            this.left = left;
            this.right = right;
        }
    }
}
