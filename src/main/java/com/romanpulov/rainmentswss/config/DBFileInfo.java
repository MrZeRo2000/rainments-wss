package com.romanpulov.rainmentswss.config;

public class DBFileInfo {
    private final String dbFileName;
    private final String dbBackupPath;

    public DBFileInfo(String dbFileName, String dbBackupPath) {
        this.dbFileName = dbFileName;
        this.dbBackupPath = dbBackupPath;
    }

    public String getDBFileName() {
        return dbFileName;
    }

    public String getDBBackupPath() {
        return dbBackupPath;
    }
}
