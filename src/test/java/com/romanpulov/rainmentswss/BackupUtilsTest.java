package com.romanpulov.rainmentswss;

import com.romanpulov.jutilscore.storage.BackupUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BackupUtilsTest {

    @Autowired
    BackupUtils backupUtils;

    @Test
    void testBackupUtils() {
        Assertions.assertNotNull(backupUtils);
    }
}
