package net.slipp.fifth.kotlin.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.codec.Base64;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.Application;
import net.slipp.fifth.kotlin.common.exception.ApplicationException;

@Slf4j
public class CommonUtilSubstr {
	public static void main(String args[]) {
		String div[] = {"일반여","어린이여","일반남","어린이남","청소년남", "청소년여"};
		for(String s : div) {
			log.debug( getKolasColDivid(s)[0]+"|" +getKolasColDivid(s)[1]);
		}
	}
	
	public static String[] getKolasColDivid(String s) {
			String str[] = new String[2];
			String f = "여";
			String m = "남";
			String j = "자";
			if(s.indexOf(f)>=0) {
				str[0] = s.substring(0,s.lastIndexOf(f));
				str[1] = s.substring(s.lastIndexOf(f))+j ;
			}else if(s.indexOf(m)>=0) {
				str[0] = s.substring(0,s.lastIndexOf(m));
				str[1] = s.substring(s.lastIndexOf(m)) +j ;
			}
			return str;
	}

}