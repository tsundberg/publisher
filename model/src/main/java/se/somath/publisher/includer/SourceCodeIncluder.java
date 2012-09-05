package se.somath.publisher.includer;

import se.somath.publisher.builder.SourceCodeBuilder;

import java.util.LinkedList;
import java.util.List;

public class SourceCodeIncluder {
    private SourceCodeBuilder sourceCodeBuilder = new SourceCodeBuilder();
    private IncludeSourceCodeParser includeSourceCodeParser = new IncludeSourceCodeParser();

    public List<String> addIncludes(List<String> content) {

        List<String> result = new LinkedList<String>();

        String includeTag = "";
        boolean inComment = false;
        boolean startTagFound = false;

        for (String candidate : content) {
            if (candidate.startsWith("<!--")) {
                inComment = true;
            }

            if (candidate.endsWith("-->")) {
                inComment = false;
            }

            if (candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment) {
                includeTag = candidate;

                includeSourceCodeParser.parse(includeTag);
                includeTag = "";

                result = getFormattedSourceCode(result, includeSourceCodeParser);
                continue;
            }

            if (candidate.startsWith("<include") && !candidate.endsWith("/>") && !inComment) {
                includeTag = candidate;
                startTagFound = true;
                continue;
            }

            if (!candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment) {
                includeTag += candidate;

                includeSourceCodeParser.parse(includeTag);
                includeTag = "";

                result = getFormattedSourceCode(result, includeSourceCodeParser);

                startTagFound = false;
                continue;
            }

            if (startTagFound && !inComment) {
                includeTag += candidate;
            } else {
                result.add(candidate);
            }
        }

        return result;
    }

    private List<String> getFormattedSourceCode(List<String> result, IncludeSourceCodeParser includeSourceCodeParser) {
        return sourceCodeBuilder.getFormattedSourceCode(result, includeSourceCodeParser);
    }

    public void setIncludeSourceCodeParser(IncludeSourceCodeParser includeSourceCodeParser) {
        this.includeSourceCodeParser = includeSourceCodeParser;
    }

    public void setSourceCodeBuilder(SourceCodeBuilder sourceCodeBuilder) {
        this.sourceCodeBuilder = sourceCodeBuilder;
    }
}