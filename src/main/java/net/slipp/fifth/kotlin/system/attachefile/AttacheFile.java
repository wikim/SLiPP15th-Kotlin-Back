package net.slipp.fifth.kotlin.system.attachefile;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Date;
import java.util.regex.Matcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.slipp.fifth.kotlin.common.listener.AttacheFileEntityListener;

@Data
@Entity
@Table(indexes = { @Index(name = "idx_attache_file_id", columnList = "id"),
        @Index(name = "idx_attache_file_file_id", columnList = "fileId") })
@NoArgsConstructor
@EntityListeners(AttacheFileEntityListener.class)
@EqualsAndHashCode(of = { "name", "contentType", "size", "fileId" })
@ToString
@JsonIgnoreProperties(value = { "filePath", "new" })
public class AttacheFile implements Serializable {
    private static final long serialVersionUID = -4223400853357968793L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String contentType;

    private Long size;

    private String path;

    /**
     * FileId: by UUID
     */
    private String fileId;
    
    @Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

    public AttacheFile(String name, String contentType, Long size, String path, String fileId) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.path = path;
        this.fileId = fileId;
        this.createdDate = new Date();
    }
	
    @PrePersist
    public void prePersist() {
        this.createdDate = new Date();
    }
    
    public boolean isNew() {
        return null == getId();
    }

    /**
     *
     * @return AttachFile.path + AttacheFile.fileId
     */
    public String getFilePath() {
    	if(StringUtils.isNotEmpty(path)) {
    		return Paths.get(path.replaceAll("(\\\\+|/+)", Matcher.quoteReplacement(File.separator)), fileId).toString();
    	} else {
    		return Paths.get(path, fileId).toString();
    	}
    }
}
