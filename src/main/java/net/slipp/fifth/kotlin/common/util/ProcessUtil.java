package net.slipp.fifth.kotlin.common.util;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang.SystemUtils;

/**
 */
public class ProcessUtil {

	public static boolean isProcessRunning(String workPath) throws IOException, InterruptedException  {
		boolean isRunning = Boolean.FALSE;
		Process proc = null;
		if(SystemUtils.IS_OS_WINDOWS) {
			proc = Runtime.getRuntime().exec("cmd /c jps -m");
		} else {
			String[] cmd = { "/bin/sh", "-c", "ps -ef | grep java | grep " + workPath + " | grep -v grep"};
			proc = Runtime.getRuntime().exec(cmd);
		}
		
		Scanner sc = new Scanner(proc.getInputStream());
		while(sc.hasNext()) {
			if(sc.nextLine().contains(workPath)) {
				isRunning = Boolean.TRUE;	
				break;
			}
		}	
		proc.getInputStream().close();
		sc.close();
		proc.waitFor();
		return isRunning;
	}
}
