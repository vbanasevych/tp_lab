package org.example.sipl.ast;

public abstract class Stm extends AstNode {

    public static class Assign extends Stm {
        public final String varName;
        public final Aexp expr;
        public Assign(String varName, Aexp expr) {
            this.varName = varName;
            this.expr = expr;
        }
    }

    public static class Skip extends Stm {}

    public static class Block extends Stm {
        public final Stm body;
        public Block(Stm body) { this.body = body; }
    }

    public static class If extends Stm {
        public final Bexp condition;
        public final Stm thenBranch;
        public final Stm elseBranch;

        public If(Bexp condition, Stm thenBranch, Stm elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }
    }

    public static class While extends Stm {
        public final Bexp condition;
        public final Stm body;

        public While(Bexp condition, Stm body) {
            this.condition = condition;
            this.body = body;
        }
    }

    public static class Comp extends Stm {
        public final Stm stm1;
        public final Stm stm2;

        public Comp(Stm stm1, Stm stm2) {
            this.stm1 = stm1;
            this.stm2 = stm2;
        }
    }
}
