package org.example.sipl.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.example.sipl.SIPLBaseVisitor;
import org.example.sipl.SIPLParser;
import org.example.sipl.ast.*;

public class AstBuilderVisitor extends SIPLBaseVisitor<AstNode> {

    // збереження координат вузла у вихідному тексті
    private <T extends AstNode> T withSpan(T node, ParserRuleContext ctx) {
        node.setSpan(new SourceSpan(ctx.start.getLine(), ctx.start.getCharPositionInLine()));
        return node;
    }

    @Override
    public AstNode visitProgram(SIPLParser.ProgramContext ctx) {
        return visit(ctx.statement());
    }

    // --- Стейтменти (Statements) ---

    @Override
    public AstNode visitAssignStm(SIPLParser.AssignStmContext ctx) {
        String varName = ctx.ID().getText();
        Aexp expr = (Aexp) visit(ctx.aexp());
        return withSpan(new Stm.Assign(varName, expr), ctx);
    }

    @Override
    public AstNode visitSkipStm(SIPLParser.SkipStmContext ctx) {
        return withSpan(new Stm.Skip(), ctx);
    }

    @Override
    public AstNode visitBlockStm(SIPLParser.BlockStmContext ctx) {
        Stm body = (Stm) visit(ctx.statement());
        return withSpan(new Stm.Block(body), ctx);
    }

    @Override
    public AstNode visitIfStm(SIPLParser.IfStmContext ctx) {
        Bexp condition = (Bexp) visit(ctx.bexp());
        Stm thenBranch = (Stm) visit(ctx.statement(0));
        Stm elseBranch = (Stm) visit(ctx.statement(1));
        return withSpan(new Stm.If(condition, thenBranch, elseBranch), ctx);
    }

    @Override
    public AstNode visitWhileStm(SIPLParser.WhileStmContext ctx) {
        Bexp condition = (Bexp) visit(ctx.bexp());
        Stm body = (Stm) visit(ctx.statement());
        return withSpan(new Stm.While(condition, body), ctx);
    }

    @Override
    public AstNode visitCompStm(SIPLParser.CompStmContext ctx) {
        Stm stm1 = (Stm) visit(ctx.statement(0));
        Stm stm2 = (Stm) visit(ctx.statement(1));
        return withSpan(new Stm.Comp(stm1, stm2), ctx);
    }

    // --- Арифметичні вирази (Aexp) ---

    @Override
    public AstNode visitParenAExpr(SIPLParser.ParenAExprContext ctx) {
        return visit(ctx.aexp()); // дужки ігноруємо
    }

    @Override
    public AstNode visitNumExpr(SIPLParser.NumExprContext ctx) {
        int value = Integer.parseInt(ctx.NUMBER().getText());
        return withSpan(new Aexp.Num(value), ctx);
    }

    @Override
    public AstNode visitVarExpr(SIPLParser.VarExprContext ctx) {
        return withSpan(new Aexp.Var(ctx.ID().getText()), ctx);
    }

    @Override
    public AstNode visitMulExpr(SIPLParser.MulExprContext ctx) {
        Aexp left = (Aexp) visit(ctx.aexp(0));
        Aexp right = (Aexp) visit(ctx.aexp(1));
        return withSpan(new Aexp.BinOp("*", left, right), ctx);
    }

    @Override
    public AstNode visitAddSubExpr(SIPLParser.AddSubExprContext ctx) {
        String op = ctx.op.getText(); // "+" або "-"
        Aexp left = (Aexp) visit(ctx.aexp(0));
        Aexp right = (Aexp) visit(ctx.aexp(1));
        return withSpan(new Aexp.BinOp(op, left, right), ctx);
    }

    // --- Логічні вирази (Bexp) ---

    @Override
    public AstNode visitParenBExpr(SIPLParser.ParenBExprContext ctx) {
        return visit(ctx.bexp());
    }

    @Override
    public AstNode visitNotExpr(SIPLParser.NotExprContext ctx) {
        Bexp expr = (Bexp) visit(ctx.bexp());
        return withSpan(new Bexp.Not(expr), ctx);
    }

    @Override
    public AstNode visitRelExpr(SIPLParser.RelExprContext ctx) {
        String op = ctx.op.getText(); // "=" або ">"
        Aexp left = (Aexp) visit(ctx.aexp(0));
        Aexp right = (Aexp) visit(ctx.aexp(1));
        return withSpan(new Bexp.RelOp(op, left, right), ctx);
    }

    @Override
    public AstNode visitOrExpr(SIPLParser.OrExprContext ctx) {
        Bexp left = (Bexp) visit(ctx.bexp(0));
        Bexp right = (Bexp) visit(ctx.bexp(1));
        return withSpan(new Bexp.Or(left, right), ctx);
    }
}
