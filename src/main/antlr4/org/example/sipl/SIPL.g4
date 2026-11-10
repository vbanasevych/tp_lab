grammar SIPL;

// 1. Точка входу в програму
program: 'begin' statement 'end' EOF ;

// 2. Оператори
statement
    : ID ':=' aexp                                # AssignStm
    | 'skip'                                      # SkipStm
    | 'begin' statement 'end'                     # BlockStm
    | 'if' bexp 'then' statement 'else' statement # IfStm
    | 'while' bexp 'do' statement                 # WhileStm
    | statement ';' statement                     # CompStm
    ;

// 3. Арифметичні вирази
// Правила розташовані в порядку спадання пріоритету
aexp
    : '(' aexp ')'                                # ParenAExpr
    | NUMBER                                      # NumExpr
    | ID                                          # VarExpr
    | aexp '*' aexp                               # MulExpr
    | aexp op=('+' | '-') aexp                    # AddSubExpr
    ;

// 4. Логічні вирази
bexp
    : '(' bexp ')'                                # ParenBExpr
    | 'not' bexp                                  # NotExpr
    | aexp op=('=' | '>') aexp                    # RelExpr
    | bexp 'or' bexp                              # OrExpr
    ;

// 5. Лексичні правила
ID      : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER  : '-'? [0-9]+ ;

// Ігноруємо пробіли, табуляції та перенесення рядків
WS      : [ \t\r\n]+ -> skip ;