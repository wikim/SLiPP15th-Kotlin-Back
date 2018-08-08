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
public class CommonUtil {
	private static final String MSG_OCCUR_EXCEPTION = "Occur Exception: {}";
	private static final String PROTOCOL_SCHEMA = "://";
	private static final String COLON = ":";

	public static long setStringParseLongIdDefaultZero(String stringNumber) {
		try {
			if(StringUtils.isNotEmpty(stringNumber)) {
				return Long.parseLong(stringNumber);
			} else {
				return 0L;
			}
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	/**
	 * 최신 파일 get
	 *
	 * @param filePath
	 * @param ext
	 * @return
	 */
	public static File getTheNewestFile(String filePath, String ext) {
		File[] files = getFilesOrderByComparator(filePath, ext, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

		File theNewestFile = null;
		if (hasFiles(files)) {
			theNewestFile = files[0];
		}

		return theNewestFile;
	}

	private static boolean hasFiles(File[] files) {
		return files != null && files.length > 0;
	}

	/**
	 * 지정된 경로에 지정한 파일형식의 파일들을 지정된 정렬방식에 따라 반환한다.
	 *
	 * @param filePath
	 * @param fileExtension
	 *            ex) txt, doc
	 * @param comparator
	 * @return
	 */
	public static File[] getFilesOrderByComparator(String filePath, String fileExtension, Comparator<File> comparator) {

		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + fileExtension);
		File[] files = dir.listFiles(fileFilter);
		if (null != files) {
			if (files.length > 0) {
				Arrays.sort(files, comparator);
			}
		}
		return files;
	}

	public static long getPercentOneAvg(long tg, long[] arr) {
		long retAvg = 0;
		double tgD = tg;
		double avg = 0;
		for (long i : arr) {
			avg += i;
		}
		retAvg = (Math.round((tgD / avg) * 100));
		return retAvg;
	}

	public static String fileSizeFormatter(long size) {
		if (size <= 0) {
			return "0";
		}

		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static String getNumberFormater(long number) {
		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(number);
	}

	public static byte[] compressGZIP(String value) throws ApplicationException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				GZIPOutputStream gzipOutStream = new GZIPOutputStream(
						new BufferedOutputStream(byteArrayOutputStream));) {

			gzipOutStream.write(value.getBytes("UTF-8"));
			gzipOutStream.finish();
			gzipOutStream.close();

			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			log.error(MSG_OCCUR_EXCEPTION, e);
			throw new ApplicationException(e);
		}
	}

	public static String decompressGZIP(byte[] value) throws ApplicationException {
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				GZIPInputStream gzipInStream = new GZIPInputStream(
						new BufferedInputStream(new ByteArrayInputStream(value)));) {
			int size = 0;
			byte[] buffer = new byte[2048];
			while ((size = gzipInStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, size);
			}
			return new String(outStream.toByteArray(), "UTF-8");
		} catch (Exception e) {
			log.error(MSG_OCCUR_EXCEPTION, e);
			throw new ApplicationException(e);
		}
	}

	public static String compressBase64Encoder(String str) {

		byte[] strenc = null;
		try {
			strenc = Base64.encode(compressGZIP(str));

		} catch (Exception e) {
			log.error(MSG_OCCUR_EXCEPTION, e);
		}
		return new String(strenc);
	}

	public static String decompressBase64Encoder(String str) {
		try {
			return decompressGZIP(Base64.decode(str.getBytes()));
		} catch (Exception e) {
			log.error(MSG_OCCUR_EXCEPTION, e);
			throw e;
		}
	}

	/**
	 * 문자열 byte 크기로 자르기(한글 구분)
	 *
	 * @param raw
	 * @param len
	 * @param encoding
	 * @return
	 * @throws Exception
	 */

	public static String subByte(String str, int len, String encoding) {
		try {
			if(StringUtils.isEmpty(str)) {
				return str;
			}
			byte[] strBytes = str.getBytes(encoding);
			int strLength = strBytes.length;
			int minusByteNum = 0;
			int offset = 0;
			int hangulByteNum = encoding.equals("UTF-8") ? 3 : 2;
			if (strLength > len) {
				minusByteNum = 0;
				offset = len;
				for (int j = 0; j < offset; j++) {
					if ((strBytes[j] & 0x80) != 0) {
						minusByteNum++;
					}
				}
				if (minusByteNum % hangulByteNum != 0) {
					offset -= minusByteNum % hangulByteNum;
				}
				return new String(strBytes, 0, offset, encoding);
			} else {
				return str;
			}
		} catch (Exception e) {
			log.error(MSG_OCCUR_EXCEPTION, e);
		}

		return null;
	}

	public static String getLocalAddress() {
		try {
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			while(ifaces.hasMoreElements()) {
				NetworkInterface iface = ifaces.nextElement();
				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while(addresses.hasMoreElements()){
					InetAddress addr = addresses.nextElement();
					if(addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
						return addr.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			log.error(MSG_OCCUR_EXCEPTION, e);
		}
		return StringUtils.EMPTY;
	}

	public static String getRequestURIAndQueryString(HttpServletRequest req) {
		StringBuffer requestURL = new StringBuffer();
        requestURL.append(req.getRequestURI());
        if (StringUtils.isNotEmpty(req.getQueryString())) {
            requestURL.append("?").append(req.getQueryString());
        }
        return requestURL.toString();
	}
	
	public static String getBase64String( String filePath, String fileName, String fileId ) throws Exception{
		 
	    if(StringUtils.isEmpty(filePath) || StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileId)) return "";

	    String fileExtName = fileName.substring( fileName.lastIndexOf(".") + 1);
	 
	    FileInputStream inputStream = null;
	    ByteArrayOutputStream byteOutStream = null;
	    
	    try
	    {    	
	        File file = new File( Paths.get( Application.getHomeDir().getFile().toString(), "persistence/" + filePath + "/" + fileId).toString() );
	 
	        if( file.exists() )
	        {
	        	inputStream = new FileInputStream( file );
	        	byteOutStream = new ByteArrayOutputStream();
	 
	            int len = 0;
	            byte[] buf = new byte[1024];
	            while( (len = inputStream.read( buf )) != -1 ) {
	                byteOutStream.write(buf, 0, len);
	            }
	 
	            byte[] fileArray = byteOutStream.toByteArray();
	            String imageString = new String( Base64.encode( fileArray ) );
	 
	            String changeString = "data:image/"+ fileExtName +";base64, "+ imageString;

	            return changeString;
	        }
	    } catch( IOException e) {
	        e.printStackTrace();
	    } finally {
	        inputStream.close();
	        byteOutStream.close();
	    }
	 
	    return "";
	}
	
	public static String transStrToHtml( String plainText) throws Exception{
		StringBuffer sb = new StringBuffer();
	    for( char c : plainText.toCharArray() ) {
	        switch(c) {
	        	case ' ': sb.append("&nbsp;"); break;
	            case '<': sb.append("&lt;"); break;
	            case '>': sb.append("&gt;"); break;
	            case '&': sb.append("&amp;"); break;
	            case '"': sb.append("&quot;"); break;
	            case '\n': sb.append("<br>"); break;
	            case '\t': sb.append("&nbsp;&nbsp;&nbsp;&nbsp;"); break;  
	            default:
	                if( c < 128 ) {
	                	sb.append(c);
	                } else {
	                	sb.append("&#").append((int)c).append(";");
	                }
	        }
	    }
	    return sb.toString();
	}
	
	public static boolean isAvailablePort(int port) {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			return true;
		} catch (IOException e) {
			log.warn("Address already in use = [{}]", port);
		} finally {
			if(Objects.nonNull(socket)) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

}