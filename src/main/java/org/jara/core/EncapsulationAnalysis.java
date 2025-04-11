package org.jara.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncapsulationAnalysis {

    public List<Attentions> startAnalysis(HashMap<String, String> classes){
         List<Attentions> attentions = new ArrayList<>();

        for (Map.Entry<String, String> entry : classes.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();

            try {
                JavaParser javaParser = new JavaParser();
                ParseResult<CompilationUnit> parseResult = javaParser.parse(content);
                if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                    CompilationUnit cu = parseResult.getResult().get();
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при проверке инкапсуляции");
            }
        }

        return attentions;
    }
}
