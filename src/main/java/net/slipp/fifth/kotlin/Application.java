package net.slipp.fifth.kotlin;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer {

    private static final String DIR_APP_ID = "server";
    private static final String YO_HOME = ".yo";
    private static final String PROPERTY_YO_HOME = "yo.home";
    private static final String HOME_NAME = "YO_HOME";
    private static final String DIR_YO = new StringBuilder().append(YO_HOME).append(File.separator)
            .append(DIR_APP_ID).toString();

    
	public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.setAdditionalProfiles(Arrays.asList(args).stream().filter(x -> StringUtils.contains(x, "mode")).map(x -> StringUtils.split(x, "=")[1]).findAny().orElse("production"));
        ConfigurableApplicationContext context = application.run(args);
        
        log.info("Server running mode is = [{}]", Arrays.asList(context.getEnvironment().getActiveProfiles()).stream().filter(x -> StringUtils.contains(x, "production")).findAny().orElse(""));
	}
	
	public static FileAndDescription getHomeDir() {
        String sysProp = System.getProperty(HOME_NAME);
        if (null != sysProp) {
            return new FileAndDescription(new File(sysProp.trim()), "System.getPropty(\"" + HOME_NAME + "\")");
        }

        File home = FileUtils.getFile(new StringBuilder().append(FileUtils.getUserDirectoryPath())
                .append(File.separator).append(DIR_YO).toString());
        return new FileAndDescription(home, "$user.home/.yo");
    }
	
	@Data
    public static class FileAndDescription {
        private final File file;
        private final String description;

        public FileAndDescription(File file, String description) {
            this.file = file;
            this.description = description;
        }
    }
}
