package com.tu.codeguard.service;

import java.util.Map;
import java.util.zip.ZipInputStream;

public interface ZipService {

    /**
     * Declaration of a method to extract specific module (controller, service, etc.) from source
     * code (.zip file)
     *
     * @param zip is the project to extract code from
     */
    Map<String, String> readSourceCode(ZipInputStream zip);
}
