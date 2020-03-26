package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.service.BackupService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceBackupTest {

    @Autowired
    BackupService backupService;

    @Test
    void testBackupService() {
        Assertions.assertNotNull(this.backupService);

        String backupResult = this.backupService.backupDatabase();
        Assertions.assertNotNull(backupResult);

        Path backupFilePath = Paths.get(backupResult);
        Assertions.assertTrue(Files.exists(backupFilePath));
    }
}
