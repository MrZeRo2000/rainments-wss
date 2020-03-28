package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.DateTimeDTO;
import com.romanpulov.rainmentswss.dto.MessageDTO;
import com.romanpulov.rainmentswss.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class AppController {

    private final BackupService backupService;

    @Autowired
    public AppController(BackupService backupService) {
        this.backupService = backupService;
    }

    @GetMapping("/app:last_backup_date_time")
    ResponseEntity<DateTimeDTO> getLastBackupDateTime() throws NotFoundException {
        LocalDateTime lastBackupDateTime = backupService.getLastModifiedBackupFileDateTime();
        if (lastBackupDateTime != null) {
            return ResponseEntity.ok(new DateTimeDTO(backupService.getLastModifiedBackupFileDateTime()));
        } else {
            throw new NotFoundException("Database backup information not found");
        }
    }

    @PostMapping("/app:backup_database")
    ResponseEntity<MessageDTO> backupDatabase() {
        String backupResult = backupService.backupDatabase();
        if (backupResult == null) {
            throw new RuntimeException("Error creating database backup");
        } else {
            return ResponseEntity.ok(new MessageDTO(backupResult));
        }
    }
}
