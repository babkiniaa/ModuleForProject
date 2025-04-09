package org.jara.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASTAnalysis {
    public List<Attentions> startAnalysis(HashMap<String, String> classes) {
        List<Attentions> attentions = new ArrayList<>();

        for (Map.Entry<String, String> entry : classes.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();

            try {
                JavaParser javaParser = new JavaParser();
                ParseResult<CompilationUnit> parseResult = javaParser.parse(content);

                if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                    CompilationUnit cu = parseResult.getResult().get();
                    MethodVisitor methodVisitor = new MethodVisitor(fileName);
                    methodVisitor.visit(cu, attentions);
                }
            } catch (Exception e) {
               throw new RuntimeException("Ошибка в AST");
            }
        }

        return attentions;
    }

    // Внутренний класс для посещения методов в AST
    private static class MethodVisitor extends VoidVisitorAdapter<List<Attentions>> {
        private final String fileName;

        public MethodVisitor(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void visit(MethodDeclaration md, List<Attentions> attentions) {
            super.visit(md, attentions);

            if (md.getBody().isPresent() && md.getBody().get().getStatements().size() > 20) {
                attentions.add(new Attentions(
                        fileName,
                        md.getRange().map(r -> r.begin.line).orElse(-1),
                        md.toString(),
                        "Метод слишком длинный, возможно, стоит разбить на несколько методов."
                ));
            }
        }
    }

}
