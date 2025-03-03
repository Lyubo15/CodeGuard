package com.tu.codeguard.enums;

public enum PromptOption {
    CODE_REVIEW("Imagine you are a senior software engineer. Conduct a step-by-step review of the code, highlighting strengths, weaknesses, and areas for improvement. Provide an overall summary of code quality. Rating: 1-10."),
    TECHNICAL_INTERVIEW("As a technical interviewer, analyze the code and identify both the good and bad parts. Highlight best practices followed and violations of fundamental programming principles. Rating: 1-10."),
    LEVEL_EVALUATION("Evaluate whether the code is written at a Junior, Mid, or Senior developer level. Justify with examples based on complexity, readability, and maintainability. Rating: 1-10."),
    CLEAN_CODE_SCORE("Is clean code implemented by adhering to naming conventions, logging, formatting, and clarity. Provide examples of well-written and poorly-written parts. Rating: 1-10."),
    MODERN_FEATURES("Assess the usage of modern language features, framework capabilities, and adherence to best practices. Are the latest language syntax and idioms used effectively? Rating: 1-10."),
    DESIGN_PATTERNS("Identify and evaluate the usage of design patterns. Detect code smells and anti-patterns that could lead to future issues. Rating: 1-10."),
    DRY("Check if the code adheres to the DRY (Don't Repeat Yourself) principle. Identify unnecessary duplication and suggest refactoring opportunities. Rating: 1-10."),
    PERFORMANCE("Identify potential memory leaks, inefficient loops, or unoptimized database queries. Suggest ways to improve performance and efficiency. Rating: 1-10."),
    ERROR_HANDLING("Evaluate how errors are handled, including try-catch blocks, logging, and fail-safe mechanisms. Suggest improvements for robustness and informative error management. Rating: 1-10."),
    SOLID("Assess adherence to SOLID principles and overall code maintainability. Provide reasoning and a rating for maintainability. Rating: 1-10."),
    HARD_CODED("Detect any hard-coded values that should be parameterized or extracted into configurations. Rating: 1-10."),
    PLATFORM_GUIDELINES("Check if the code follows platform-specific best practices (e.g., Java, .NET, Python, React, etc.). Rating: 1-10."),
    REDUNDANCY("Identify redundant code and suggest ways to refactor and simplify. Rating: 1-10."),
    UNIT_TESTS("Review unit test coverage, structure, and mocking/stubbing practices. Are edge cases covered effectively? Rating: 1-10."),
    SCALABILITY("Evaluate how well the code is designed for scalability. Can it handle growth in data, traffic, and concurrent users? Rating: 1-10."),
    EXTENSIBILITY("Assess whether new features can be added with minimal impact on existing code. Identify rigid parts that hinder extensibility. Rating: 1-10."),
    ASYNC_HANDLING("Evaluate how the code handles asynchronous operations (callbacks, promises, async/await). Identify potential race conditions or deadlocks. Rating: 1-10."),
    RESTFUL_ENDPOINTS("Review the structure of RESTful endpoints, ensuring proper use of HTTP methods, resource naming, and REST conventions. If not applicable, return N/A. Rating: 1-10."),
    DEPENDENCY_INJECTION("Evaluate the usage of dependency injection (constructor, setter, or field injection). Identify circular dependencies and suggest improvements. Rating: 1-10."),
    DOCUMENTATION("Check whether the code is well-documented with useful, concise, and necessary comments. Rating: 1-10."),
    TEST_STRUCTURE("Review the structure and organization of test classes. Are tests named properly and organized correctly? Rating: 1-10."),
    MODULARITY("Assess if the code is modular and promotes reusability across different application parts. Rating: 1-10."),
    COMPONENT_BOUNDARIES("Check if there are clear boundaries between this component and other components. Rating: 1-10."),
    STATE_MANAGEMENT("Evaluate how the component's state is managed (local state, context, Redux, etc.). Is state properly initialized, updated, and cleaned up? Rating: 1-10."),
    LIFECYCLE_HANDLING("If applicable, check the proper use of lifecycle methods/hooks (e.g., componentDidMount in React, ngOnInit in Angular). Rating: 1-10."),
    CLEANUP_HANDLING("Evaluate if component unmounting and cleanup are handled properly (e.g., unsubscribing from event listeners). Rating: 1-10.");

    private final String promptText;

    PromptOption(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptText() {
        return promptText;
    }
}