package com.romanpulov.rainmentswss.service;

import com.romanpulov.jutilscore.storage.BackupUtils;
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

    public LocalDateTime getLastModifiedBackupFileDateTime() {
        BackupUtils backupUtils = backupUtilsObjectProvider.getObject();

        File[] backupFiles = backupUtils.getBackupFiles();
        if (backupFiles != null) {
            List<File> fileList = Arrays.asList(backupUtils.getBackupFiles());
            Optional<Long> lastModified = fileList.stream().map(File::lastModified).max(Long::compareTo);

            if (lastModified.isPresent()) {
                Date lastModifiedDate = new Date(lastModified.get());
                return lastModifiedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
