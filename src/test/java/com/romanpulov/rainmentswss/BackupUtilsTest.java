package com.romanpulov.rainmentswss;

import com.romanpulov.jutilscore.storage.BackupUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BackupUtilsTest {

    @Autowired
    BackupUtils backupUtils;

    @Test
    void testBackupUtils() {
        Assertions.assertNotNull(backupUtils);
    }
}
