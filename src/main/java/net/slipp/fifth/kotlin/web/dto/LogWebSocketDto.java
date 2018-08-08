/**
 * 
 */
package net.slipp.fifth.kotlin.web.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogWebSocketDto implements Serializable {
    private static final long serialVersionUID = -7734201634959223647L;

    private long projectId;
    private long jobId;
    private StringBuffer contents;
    private long lastModifyMilliSec;
    private int lastLineNumber;
    private int TotalLineNumber;

}
