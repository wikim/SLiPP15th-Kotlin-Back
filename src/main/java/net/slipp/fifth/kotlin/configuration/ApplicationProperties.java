package net.slipp.fifth.kotlin.configuration;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.util.StringUtils;

import lombok.Data;

@Data
@Validated
@Configuration
@PropertySource(value = "classpath:properties/yo.properties")
@ConfigurationProperties(prefix = "yo")
public class ApplicationProperties {
    private String applicationName;
    private String applicationVersion;
    private String homeDir;
    private String identifier;
    private String dbBackupFileName;
    private String homePropertyKey;
    /**
     * AttacheFile max UploadFile size
     */
    private Long maxUploadFileSize;

    private Scheduler scheduler;

    private Path paths;

    @Data
    public static class Path {
        /**
         * Member's profile image storepath
         */
        private String profiles;

        /**
         * Inno Quartz license key path
         */
        private String license;

        /**
         * Attache default Temporary Path
         */
        private String temporary;
        /**
         * AttacheFile default persistence Path
         */
        private String persistence;

        /**
         * uploaded backup file for restore
         */
        private String restore;

        /**
         * Path of {@link Project}
         */
        private String projects;
        
        /**
         * plugins Path
         */
        private String plugins;
        
        /**
         * ha Path
         */
        private String ha;
        
    }

    @Data
    public static class Scheduler {
        /**
         * use QuartzScheduler name
         */
        private String name;

        /**
         * use Quartz Scheduler groupName
         */
        private String groupName;

        /**
         * use Quartz Scheculer CronTrigger cronExpression
         */
        private String cronExpression;
    }

    public String getHomeDir() {
        Assert.hasText(this.homeDir);
        Assert.hasText(this.identifier);

        return Paths.get(this.homeDir, this.identifier).toString();
    }

    /**
     */
    public String getHomeDirPath() {
        Assert.hasText(this.homeDir);
        if(StringUtils.isEmpty(System.getProperty(homePropertyKey))) {
        	return Paths.get(FileUtils.getUserDirectoryPath(), getHomeDir()).toString();
        } else {
        	return System.getProperty(homePropertyKey);
        }
    }

    /**
     */
    public String getPersistencePath() {
        Assert.notNull(this.paths);
        return Paths.get(getHomeDirPath(), getPaths().getPersistence()).toString();
    }
    
    /**
     */
    public String getTemporaryPath() {
        Assert.notNull(this.paths);
        return Paths.get(getHomeDirPath(), getPaths().getTemporary()).toString();
    }

    
}
