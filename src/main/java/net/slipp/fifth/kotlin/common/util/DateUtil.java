package net.slipp.fifth.kotlin.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import net.slipp.fifth.kotlin.common.KeywordCondition;

public class DateUtil {

    // private static final String timeZoneID = "Asia/Seoul";

    static SimpleDateFormat sDateEFmt = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.ENGLISH);
    static SimpleDateFormat sDateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    static SimpleDateFormat sDateMsFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
    static DateTimeFormatter jDateFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.Z");

    /**
     *
     * @param dateStr
     * @param fmt
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String fmt) throws ParseException {
        SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
        return dateFmt.parse(dateStr);
    }

    /**
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String dateStr) throws ParseException {
        return sDateFmt.parse(dateStr);
    }

    /**
    *
    * @param dateStr
    * @return
    * @throws ParseException
    */
   public static Date strToMsDate(String dateStr) throws ParseException {
       return sDateMsFmt.parse(dateStr);
   }

    /**
     *
     * @param date
     * @param fmt
     * @return
     * @throws ParseException
     */
    public static String dateToStrFmt(Date date, String fmt) throws ParseException {
        SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
        return dateFmt.format(date);
    }

    /**
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String dateToStrFmt(Date date) throws ParseException {
        return Objects.nonNull(date) ? sDateFmt.format(date) : StringUtils.EMPTY;
    }

    /**
    *
    * @param date
    * @return
    * @throws ParseException
    */
   public static String dateToStrMsFmt(Date date) throws ParseException {
       return sDateMsFmt.format(date);
   }

    /**
     *
     * @param dateStr
     * @param fmt
     * @return
     * @throws ParseException
     */
    public static String strToDateFmt(String dateStr, String fmt) throws ParseException {
        SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
        return dateFmt.format(dateFmt.parse(dateStr));
    }

    /**
    *
    * @param dateStr
    * @param fmt
    * @return Date
    * @throws ParseException
    */
   public static Date strFmtToDate(String dateStr, String fmt) throws ParseException {
       SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
       return dateFmt.parse(dateStr);
   }

    /**
     *
     * @param dateStr
     * @param fmt
     * @return
     * @throws ParseException
     */
    public static String strToDateFmt(String dateStr) throws ParseException {
        return sDateFmt.format(sDateFmt.parse(dateStr));
    }

    public static int getDateHour(String dateStr) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(dateStr));
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return hours;
    }

    public static int getDateMinute(String dateStr) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(dateStr));
        int minutes = calendar.get(Calendar.MINUTE);
        return minutes;
    }

    public static int getLongHour(long millis) throws ParseException {

        return (int) (millis / (1000 * 60 * 60)) % 60;
    }

    public static int getLongMinute(long millis) throws ParseException {

        return (int) (TimeUnit.MILLISECONDS.toMinutes(millis));
    }

    public static String setFmtHHmmssWithColone(long millis) {
    	Period period = new Period(millis);
        return String.format("%02d:%02d:%02d", period.toStandardHours().getHours(), period.getMinutes(), period.getSeconds());
    }

    /**
     * y축
     *
     * @param millis
     * @return
     */
    public static String setLongToHourDeflection(long millis) {
        // 전체값(60분)에서 일부값(15)은 몇 퍼센트?
        int minDef = (((int) (millis / 1000 / 60) % 60) / 60) * 100;
        int hour = (int) (millis / 1000 / 60 / 60) % 60;

        return String.format("%d.%d", hour, minDef);
    }
    
    public static String getCurrentDate(String fmt) {
    	
    	GregorianCalendar today = new GregorianCalendar();
    	SimpleDateFormat format = new SimpleDateFormat(fmt);
    	return format.format(today.getTime()).toString();
    }

    /**
     * 60분을 백분율로 환산 한다.
     * @param min
     * @return
     */
    public static int getHourPercentFromMinute(int min) {
        //전체값(60분) 에서 일부값(15)은 몇 퍼센트??
        return Math.round((float) min / 60 * 100);
    }

    public static Date previusDay(int day) {
        return DateTime.now().minusDays(day).toDate();
    }

    public static SimpleDateFormat getSDateFmt(){
        return sDateFmt;
    }

    public static DateTimeFormatter getJDateFmt(){
        return jDateFmt;
    }

    public static String getToDateTime(){
    	LocalDateTime timePoint = LocalDateTime.now();

    	return timePoint.toString();
    }

    public static Date addDays(Date curDate, int term){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(curDate);
        cal.add(Calendar.DATE, term);
    	return cal.getTime();
    }

    public static String getDefaultTimeZoneOffsetStr() throws ParseException {

    	long time = getDefaultTimeZoneOffset();

    	Period period = new Period(time);
        String strTime = String.format("%02d:%02d", period.toStandardHours().getHours(), period.getMinutes());

    	if(time >= 0)
    		return "+" + strTime;
    	else
    		return strTime;
    }

    public static long getDefaultTimeZoneOffset() throws ParseException {
    	return TimeZone.getDefault().getOffset(Calendar.ZONE_OFFSET);
    }

    public static String getDefaultTimeZoneString() throws ParseException {
    	return TimeZone.getDefault().getID();
    }
    
    public static Date localDateToDate(LocalDate localDate) {
    	return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    
    //도서관 기준 하루 전일 셋팅 
    public static String setWorkingDate(String date) {
    	String pDate = date; 
    	String cDate = DateUtil.getCurrentDate("yyyy-MM-dd");
    	if(cDate.equals(pDate)) {
    		try {
				Date dDate = DateUtil.addDays(DateUtil.strFmtToDate(pDate,"yyyy-MM-dd"),-1);
				pDate = DateUtil.dateToStrFmt(dDate, "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	return pDate;
    }
    public static KeywordCondition setDate(Optional<String> pDt) {
    	KeywordCondition k = new KeywordCondition();
    	int FIRST_LEGACY_YEAR = 2006;
    	
		if (!pDt.isPresent()) {
			k.setPYear(DateUtil.getCurrentDate("yyyy").toString());
			k.setPDate(DateUtil.getCurrentDate("yyyy-MM-dd").toString());
		} else {
			String pDtGet = pDt.get();
			if (pDtGet.length() == 4)
				pDtGet += DateUtil.getCurrentDate("-MM-dd").toString();
			k.setPYear(pDtGet.substring(0, pDtGet.indexOf("-")));
			k.setPDate(pDtGet);
		}

		int cYear = Integer.parseInt(DateUtil.getCurrentDate("yyyy"));
		while (FIRST_LEGACY_YEAR <= cYear) {
			k.setPYears(cYear + "");
			cYear--;
			if (cYear == 2006)
				break;
		}
		return k;
	}
    
    public static long diffDateDay(String frmDt, String toDt) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date FromDate;
            Date ToDate;
            long calDateDays  = 0L;
			try {
				FromDate = format.parse(frmDt);
				ToDate = format.parse(toDt);
				 long calDate = ToDate.getTime() - FromDate.getTime(); 
				  calDateDays = calDate / ( 24*60*60*1000); 
			} catch (ParseException e) {
				e.printStackTrace();
			}
            calDateDays = Math.abs(calDateDays);
            return calDateDays;
    }
    
    public static List<String> getPyears() {
    	int cYear = Integer.parseInt(DateUtil.getCurrentDate("yyyy"));
    	List<String> pYears = new ArrayList<String>(); 
    	while(2017 <= cYear) {
    		pYears.add(cYear+"");
    		cYear--;
    		if(cYear == 2018) break;
    	}
    	return pYears;
    }
    
    public static String[] getWeekKr() {
    	return new String[]{"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};
    }
    
    public static List<LocalDate> getAllDays() { 
    	
    	LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
    	LocalDate endDate = LocalDate.now();
    	
	    long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate); 
	    return IntStream.iterate(0, i -> i + 1)
	      .limit(numOfDaysBetween)
	      .mapToObj(i -> startDate.plusDays(i))
	      .collect(Collectors.toList()); 
	}


}