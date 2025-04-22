package org.jara.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncapsulationAnalysis {

    public List<Attentions> startAnalysis(HashMap<String, String> classes) {
        List<Attentions> attentions = new ArrayList<>();
        JavaParser javaParser = new JavaParser();

        for (Map.Entry<String, String> entry : classes.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();

            try {
                ParseResult<CompilationUnit> parseResult = javaParser.parse(content);

                if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                    CompilationUnit cu = parseResult.getResult().get();
                    List<FieldDeclaration> fields = cu.findAll(FieldDeclaration.class);

                    for (FieldDeclaration field : fields) {
                        if (!isPrivateField(field)) {
                            attentions.add(new Attentions(fileName, field.getRange().get().getLineCount(), field.getTokenRange().get().toString(), "Поле не Является private"));
                        }
                    }
                    List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

                    for (MethodDeclaration method : methods) {
                        if (!isPublicMethod(method)){
                            attentions.add(new Attentions(fileName, method.getRange().get().getLineCount(), method.getTokenRange().get().toString(), "Метод не помечен, как public"));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при проверке инкапсуляции");
            }
        }

        return attentions;
    }

    public boolean isPrivateField(FieldDeclaration fieldDeclaration) {
        if (fieldDeclaration.getChildNodes().get(0) != null && fieldDeclaration.getChildNodes().get(0).equals("private")) {

            return true;
        } else {

            return false;
        }
    }

    public boolean isPublicMethod(MethodDeclaration methodDeclaration) {
        if (methodDeclaration.getModifiers().get(0) != null && methodDeclaration.getModifiers().get(0).equals("public")) {

            return true;
        } else {

            return false;
        }


    }


}
