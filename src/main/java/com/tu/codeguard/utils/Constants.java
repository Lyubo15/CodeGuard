package com.tu.codeguard.utils;

import java.util.List;

public class Constants {
    public static final String CLAMAV_NOT_ENABLED = "ClamAV is not enabled";
    public static final String CLAMAV_FILE_CLEAN = "File is clean";
    public static final String FILE_MALICIOUS_EXCEPTION = "MaliciousFileException has been thrown during scanning file with name %s";
    public static final String FILE_IO_EXCEPTION = "IOException has been thrown during scanning file with name %s.";

    public static final String CODE_GUARD_TEAM = "CodeGuard";
    public static final String CODE_GUARD_MAIL = "support@codeguard.com";

    public static final String ADMIN_TITLE = "Code Guard Admin REST API";

    public static final List<String> allowedExtensions = List.of("java", "js", "jsx", "ts", "tsx", "vue", "svelte", "cs", "cshtml", "csx", "py", "kt");

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

}
