package com.romanpulov.rainmentswss.service;

import com.romanpulov.jutilscore.storage.BackupUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
