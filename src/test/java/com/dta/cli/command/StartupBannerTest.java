package com.dta.cli.command;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartupBannerTest {
    @Test
    void testPrint_OutputsCorrectBanner() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        StartupBanner.print();

        String output = outContent.toString();
        assertTrue(output.contains("DIGITAL THERAPY ASSISTANT"));
        
        System.setOut(System.out);
    }
}