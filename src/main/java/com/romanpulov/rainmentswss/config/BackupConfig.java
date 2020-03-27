package com.romanpulov.rainmentswss.config;

import com.romanpulov.jutilscore.storage.BackupUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BackupConfig {
    private final DBFileInfo dbFileInfo;

    @Autowired
    public BackupConfig(DBFileInfo dbFileInfo) {
        this.dbFileInfo = dbFileInfo;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BackupUtils backupUtils() {
        return new BackupUtils(dbFileInfo.getDBFileName(), dbFileInfo.getDBBackupPath(), dbFileInfo.getBackupFileName());
    }
}
