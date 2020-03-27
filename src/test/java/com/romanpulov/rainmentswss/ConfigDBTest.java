package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.config.DBProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigDBTest {

    @Autowired
    DBProperties dbProperties;

    @Test
    void testDBProperties() {
        Assertions.assertEquals("rainments-db-backup", dbProperties.getBackupFileName());
    }
}
