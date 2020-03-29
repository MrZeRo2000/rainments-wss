package com.romanpulov.rainmentswss.service;

import com.romanpulov.jutilscore.storage.BackupUtils;
import com.romanpulov.rainmentswss.dto.BackupDatabaseInfoDTO;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BackupService {
    private final ObjectProvider<BackupUtils> backupUtilsObjectProvider;

    @Autowired
    public BackupService(ObjectProvider<BackupUtils> backupUtilsObjectProvider) {
        this.backupUtilsObjectProvider = backupUtilsObjectProvider;
    }

    public String backupDatabase() {
        BackupUtils backupUtils = backupUtilsObjectProvider.getObject();
        return backupUtils.createRollingLocalBackup();
    }

    public BackupDatabaseInfoDTO getBackupDatabaseInfo() {
        BackupUtils backupUtils = backupUtilsObjectProvider.getObject();
        int backupFileCount = 0;
        LocalDateTime lastBackupDateTime = null;

        File[] backupFiles = backupUtils.getBackupFiles();
        if ((backupFiles != null) && ((backupFileCount = backupFiles.length) > 0)) {
            List<File> fileList = Arrays.asList(backupUtils.getBackupFiles());
            Optional<Long> lastModified = fileList.stream().map(File::lastModified).max(Long::compareTo);

            if (lastModified.isPresent()) {
                Date lastModifiedDate = new Date(lastModified.get());
                lastBackupDateTime = lastModifiedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        }

        return new BackupDatabaseInfoDTO(lastBackupDateTime, backupFileCount);
    }
}
