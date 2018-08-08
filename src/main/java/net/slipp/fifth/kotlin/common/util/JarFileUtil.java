package net.slipp.fifth.kotlin.common.util;

import java.io.IOException;
import java.util.jar.JarFile;

import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JarFileUtil {
    private static final String JAVA_SPECIFICATION_VERSION = "java.specification.version";
    private static final String JAR_MANIFEST_MAIN_CLASS = "Main-Class";

    /**
     * JarFile 내부에 MainClassManifest 정보를 가지고 있는지 확인 하여 executable jar 인지 확인
     * 
     * @param jarFile
     * @return true: yes/ false: no
     * @throws IOException
     */
    public static boolean isExecutableJar(JarFile jarFile) throws IOException {
        Assert.notNull(jarFile);
        log.debug("JarFile: {}, Java version: {}", jarFile.getName(), getJavaVersion());

        if (isOlderJava7Version()) {
            log.debug("Has Main-Class: {}", jarFile.getManifest().getEntries().containsKey(JAR_MANIFEST_MAIN_CLASS));
            return jarFile.getManifest().getEntries().containsKey(JAR_MANIFEST_MAIN_CLASS);
        } else {
            log.debug("Has Main-Class: {}",
                    jarFile.getManifest().getMainAttributes().getValue(JAR_MANIFEST_MAIN_CLASS));
            return null != jarFile.getManifest().getMainAttributes().getValue(JAR_MANIFEST_MAIN_CLASS);
        }
    }

    /**
     * JDK7 이전버전가?
     * 
     * @return true: yes / false: no
     */
    private static boolean isOlderJava7Version() {
        double javaOldVersion = 1.7;
        return javaOldVersion > getJavaVersion();
    }

    private static double getJavaVersion() {
        return Double.parseDouble(System.getProperty(JAVA_SPECIFICATION_VERSION));
    }
}
