package com.romanpulov.rainmentswss.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.core.env.Environment;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@PropertySource("classpath:db.properties")
public class DBConfig {
    private final Environment env;
    private final ServletContext context;

    public DBConfig(@Autowired Environment env, @Autowired ServletContext context) {
        this.env = env;
        this.context = context;
    }

    @Bean
    public DBFileInfo dbFileInfo() {
        String dbUrl = context.getInitParameter("db-url");
        String dbFileName = dbUrl.split(":")[2];
        String dbBackupPath =  context.getInitParameter("db-backup-path");

        return new DBFileInfo(dbFileName, dbBackupPath);
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("driverClassName")));
        dataSource.setUrl(context.getInitParameter("db-url"));
        dataSource.setUsername(env.getProperty("username"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }
}
