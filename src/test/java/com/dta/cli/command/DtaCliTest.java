package com.dta.cli;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DtaCliTest {
    @Test
    void testRun_ExecutesAndExitsCleanly() {
        InputStream originalIn = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(in);

        try {
            DtaCli cli = new DtaCli();
            assertDoesNotThrow(cli::run);
        } finally {
            System.setIn(originalIn);
        }
    }
}
