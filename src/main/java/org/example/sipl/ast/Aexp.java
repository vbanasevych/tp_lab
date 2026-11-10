package org.example.sipl.ast;

public abstract class Aexp extends AstNode {

    public static class Num extends Aexp {
        public final int value;
        public Num(int value) { this.value = value; }
    }

    public static class Var extends Aexp {
        public final String name;
        public Var(String name) { this.name = name; }
    }

    public static class BinOp extends Aexp {
        public final String op; // "*", "+", "-"
        public final Aexp left;
        public final Aexp right;

        public BinOp(String op, Aexp left, Aexp right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }
    }
}
