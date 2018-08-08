package net.slipp.fifth.kotlin.system.batch;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 */
@NoArgsConstructor
public class BatchJob implements Serializable {
	
	private static final long serialVersionUID = -3133385329369498040L;
	
	
	private String jobFileName;
	private String jobFilePath;
	
	public String getJobFileName() {
		return jobFileName;
	}
	public void setJobFileName(String jobFileName) {
		this.jobFileName = jobFileName;
	}
	public String getJobFilePath() {
		return jobFilePath;
	}
	public void setJobFilePath(String jobFilePath) {
		this.jobFilePath = jobFilePath;
	}
	
	
	
}
