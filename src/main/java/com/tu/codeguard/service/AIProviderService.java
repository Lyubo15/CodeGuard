package com.tu.codeguard.service;

import java.util.UUID;

public interface AIProviderService {

    /**
     * Declaration of a method to get the defined max total number of token according to each AI model.
     *
     * @return Max total number of token to construct the message
     */
    Integer getMaxTokens();

    /**
     * Declaration of a method to calculate the tokens of the text.
     *
     * @param text is the text to calculate tokens on.
     */
    int getTokens(String text);

    /**
     * Declaration of a method to send source code to the Provider Model.
     *
     * @param message       are the message that we want to send to the Provider Model.
     * @param prompts       are the prompts that we want to send to the Provider Model.
     * @param correlationId    the correlation id we can use to correlate with analysis process
     */
    String sendMessages(String message, String prompts, UUID correlationId) throws InterruptedException;
}
