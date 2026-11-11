package org.example.sipl.cli;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.example.sipl.SIPLLexer;
import org.example.sipl.SIPLParser;
import org.example.sipl.ast.AstNode;
import org.example.sipl.parser.AstBuilderVisitor;

import java.io.File;
import java.io.IOException;

public class SiplParserUtils {

    public static AstNode parseFile(File file) throws IOException {
        SIPLLexer lexer = new SIPLLexer(CharStreams.fromPath(file.toPath()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SIPLParser parser = new SIPLParser(tokens);

        var tree = parser.program();
        return new AstBuilderVisitor().visit(tree);
    }
}
