package dev.realworld.systemtest.boundary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GeneratedSampleRemovalTest {

    @Test
    void generatedExternalSampleClientIsAbsent() {
        assertThrows(ClassNotFoundException.class, () -> Class.forName("dev.realworld.MyRemoteService"));
    }
}
