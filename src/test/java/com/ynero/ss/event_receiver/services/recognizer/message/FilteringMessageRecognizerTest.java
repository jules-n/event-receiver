package com.ynero.ss.event_receiver.services.recognizer.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

class FilteringMessageRecognizerTest {

    private Object[] nullArgs;
    private Object[] args;
    private Object[] emptyArgs;
    private MessageRecognizer recognizer;
    private String expectedValue;

    @BeforeEach
    void setUp() {
       recognizer = new FilteringMessageRecognizer();
       emptyArgs = new Object[]{};
       nullArgs = null;
       expectedValue = "Should be returned";
       args = new Object[]{null, expectedValue, new Object()};
    }

    @Test
    void filterMessageShouldReturnExceptionWhenGivenEmptyArgs() {
        assertThrows(IllegalArgumentException.class, () -> recognizer.getMessage(emptyArgs));
    }

    @Test
    void filterMessageShouldReturnExceptionWhenGivenNullArgs() {
        assertThrows(IllegalArgumentException.class, () -> recognizer.getMessage(nullArgs));
    }

    @Test
    void filterMessageShouldReturnValueWhenGivenNormalArray() {
        assertThat(recognizer.getMessage(args)).isEqualTo(expectedValue);
    }

}