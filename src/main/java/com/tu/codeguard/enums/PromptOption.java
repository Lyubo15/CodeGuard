package com.tu.codeguard.enums;

public enum PromptOption {
    CODE_REVIEW("Imagine you are a senior software engineer. Let's step by step review the code."),
    TECHNICAL_INTERVIEW("As a technical interviewer, show me the good and bad parts regarding best practices."),
    LEVEL_EVALUATION("Evaluate whether the code is written at a Junior, Mid, or Senior level."),
    CLEAN_CODE_SCORE("Score the codebase on clean code from 1/5 with examples (naming, logging, etc.)."),
    MODERN_FEATURES("Score from 1 to 5 regarding modern language features, framework usage, and adherence to best practices."),
    DESIGN_PATTERNS("Evaluate usage of design patterns, code smells, and anti-patterns."),
    DRY("Check if the code adheres to the DRY (Don't Repeat Yourself) principle."),
    PERFORMANCE("Identify potential memory leaks, resource management issues, and performance bottlenecks."),
    ERROR_HANDLING("Evaluate error-handling mechanisms and suggest improvements."),
    SOLID("Assess adherence to SOLID principles and overall maintainability."),
    HARD_CODED("Check for hard-coded values."),
    PLATFORM_GUIDELINES("Assess compliance with platform-specific coding guidelines."),
    REDUNDANCY("Check for code redundancy and opportunities for refactoring."),
    UNIT_TESTS("Review unit test coverage, structure, and naming conventions."),
    SCALABILITY("Evaluate how well the code is designed for scalability and growth."),
    EXTENSIBILITY("Assess whether new features can be added with minimal impact."),
    ASYNC_HANDLING("Evaluate the handling of asynchronous operations."),
    RESTFUL_ENDPOINTS("Review the design of RESTful endpoints (if applicable)."),
    DEPENDENCY_INJECTION("Evaluate the usage of dependency injection (constructor, setter, or field injection)."),
    DOCUMENTATION("Has the code been documented? Provide insights.");

    private final String promptText;

    PromptOption(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptText() {
        return promptText;
    }
}
