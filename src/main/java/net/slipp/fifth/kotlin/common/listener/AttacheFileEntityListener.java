package net.slipp.fifth.kotlin.common.listener;

import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.AutowireHelper;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFile;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFileService;

@Slf4j
@Component
public class AttacheFileEntityListener {

    @Autowired
    @Getter
    private AttacheFileService attacheFileService;

    @PreRemove
    public void deletePhysicalFile(AttacheFile attacheFile) {
        log.debug("PreRemove AttacheFile: {}", attacheFile);
        AutowireHelper.autowire(this, this.attacheFileService);
        attacheFileService.deleteFileFromPersistencePath(attacheFile);
    }
}
