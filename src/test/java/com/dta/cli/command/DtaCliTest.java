package com.dta.cli.command;

import com.dta.cli.DtaCli;
import com.dta.service.AuthService;
import com.dta.service.CrisisService;
import com.dta.service.DiaryService;
import com.dta.service.ProgressService;
import com.dta.service.SessionService;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class DtaCliTest {
    @Test
    void testRun_ExecutesAndExitsCleanly() {
        InputStream originalIn = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("7\n".getBytes());
        System.setIn(in);

        try {
            DtaCli cli = new DtaCli(
                    mock(AuthService.class),
                    mock(SessionService.class),
                    mock(DiaryService.class),
                    mock(ProgressService.class),
                    mock(CrisisService.class));
            assertDoesNotThrow(cli::run);
        } finally {
            System.setIn(originalIn);
        }
    }
}
