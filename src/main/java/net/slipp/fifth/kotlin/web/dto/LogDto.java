/**
 * 
 */
package net.slipp.fifth.kotlin.web.dto;

import java.io.Serializable;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class LogDto implements Serializable {
    private static final long serialVersionUID = -4055088386525446595L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private String filePath;
    private String fileName;
    private String status;
    private Long size;
    private List<String> message;
    private Date createDate;

    public LogDto() {
        this.createDate = Calendar.getInstance().getTime();
    }

    public String getCreateTimestamp() {
        return sdf.format(getCreateDate());
    }

    public String getFullFilePath() {
        Assert.hasText(this.filePath);
        Assert.hasText(this.fileName);

        return Paths.get(getFilePath(), getFileName()).toString();
    }
}
