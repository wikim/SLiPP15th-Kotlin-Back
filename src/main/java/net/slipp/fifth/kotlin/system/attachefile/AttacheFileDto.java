package net.slipp.fifth.kotlin.system.attachefile;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "name", "contentType", "size", "path", "fileId" })
public class AttacheFileDto implements Serializable {
    private static final long serialVersionUID = 2472991744543003875L;

    private Long id;
    private String name;
    private String contentType;
    private Long size;
    private String path;
    private String fileId;

    public boolean isNew() {
        return null == id;
    }

    public AttacheFile transformEntity() {
        AttacheFile attacheFile = new AttacheFile();
        
        attacheFile.setId(getId());
        attacheFile.setName(getName());
        attacheFile.setContentType(getContentType());
        attacheFile.setSize(getSize());
        attacheFile.setPath(getPath());
        attacheFile.setFileId(getFileId());
        
        return attacheFile;
    }
}
