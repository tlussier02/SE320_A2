package com.dta.cli;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DtaCliTest {
    @Test
    void testRun_ExecutesAndExitsCleanly() {
        // Provide fake input "1"
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(in);

        DtaCli cli = new DtaCli();

        // Now we just ensure it runs and finishes without crashing
        assertDoesNotThrow(() -> cli.run());

        System.setIn(System.in);
    }
}