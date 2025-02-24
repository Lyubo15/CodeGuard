package com.tu.codeguard.utils;

public final class CodeMinifier {
    private CodeMinifier() { throw new IllegalStateException("Static class"); }

    public static String minifyCode(String code) {
        StringBuilder minifiedCode = new StringBuilder();
        String[] lines = code.split("\\n");
        boolean isMultilineComment = false;

        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }

            if (!isMultilineComment) {
                isMultilineComment = isMultilineCommentBegin(line);
            }

            boolean isImportLine = isImportLine(line);

            if (isMultilineComment || isImportLine) {
                if (!isImportLine) {
                    isMultilineComment = !isMultilineCommentEnd(line);
                }
                continue;
            }

            line = removeSingleLineComments(line);
            line = reduceWhitespace(line);

            minifiedCode.append(line);
        }

        return minifiedCode.toString();
    }

    private static boolean isMultilineCommentBegin(String line) {
        return line.contains("/*");
    }

    private static boolean isMultilineCommentEnd(String line) {
        return line.contains("*/");
    }

    private static boolean isImportLine(String line) {
        String trimmedLine = line.trim();
        return trimmedLine.startsWith("import ") || trimmedLine.startsWith("package ") || trimmedLine.startsWith("include ");
    }

    private static String removeSingleLineComments(String line) {
        return line.replaceAll("//.*", "");
    }

    private static String reduceWhitespace(String line) {
        return line.replaceAll("\\s+", " ").trim();
    }
}
