package com.dta.cli.command;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartupBannerTest {
    @Test
    void testPrint_OutputsCorrectBanner() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            StartupBanner.print();
            String output = outContent.toString();
            assertTrue(output.contains("DIGITAL THERAPY ASSISTANT"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
