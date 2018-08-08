package net.slipp.fifth.kotlin.common.util;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.Holiday;

public class HolyDaySample {


	    static Logger logger = LoggerFactory.getLogger(Holiday.class);

	    public static void main(String[] args) throws Exception {
//	        long date = DateUtils.parseDate("20180507", "yyyyMMdd").getTime();
	        long date = DateUtils.parseDate("20180217", "yyyyMMdd").getTime();
//	        long date = DateUtils.parseDate("20171006", "yyyyMMdd").getTime();
	        boolean isLegalHoliday = isLegalHoliday(date);
	        boolean isWeekend = isWeekend(date);
	        boolean isAlternative = isAlternative(date);
	        boolean isHoliday = isHoliday(date);
	        
	        System.out.println("isLegalHoliday:"+isLegalHoliday);
	        System.out.println("isWeekend:"+isWeekend);
	        System.out.println("isAlternative:"+isAlternative);
	        System.out.println("isHoliday:"+isHoliday);
	    }
	    
	    /**
	     * 공휴일 여부
	     * @param date
	     */
	    public static boolean isHoliday(long date) {
	        return isLegalHoliday(date) || isWeekend(date) || isAlternative(date);
	    }
	    
	    /**
	     * 음력날짜 구하기
	     * @param date
	     * @return
	     */
	    public static String getLunarDate(long date) {
	        ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));
	        String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
	        m = StringUtils.leftPad(m, 2, "0");
	        String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
	        d = StringUtils.leftPad(d, 2, "0");
	        
	        return m + d;
	    }
	    
	    /**
	     * 법정휴일
	     * @param date
	     * @return
	     */
	    public static boolean isLegalHoliday(long date) {
	        boolean result = false;
	        
	        String[] solar = {"0101", "0301", "0505", "0606", "0815", "1225"};
	        String[] lunar = {"0101", "0102", "0408", "0814", "0815", "0816", "1230"};
	        
	        List<String> solarList = Arrays.asList(solar);
	        List<String> lunarList = Arrays.asList(lunar);
	        
	        String solarDate = DateFormatUtils.format(date, "MMdd");
	        ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));
	        
//	        String y = String.valueOf(cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637);
	        String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
	        m = StringUtils.leftPad(m, 2, "0");
	        String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
	        d = StringUtils.leftPad(d, 2, "0");
	        
	        String lunarDate = m + d;
	        
	        if (solarList.indexOf(solarDate) >= 0) {
	            return true;
	        }
	        if (lunarList.indexOf(lunarDate) >= 0) {
	            return true;
	        }
	        
	        return result;
	    }
	    
	    /**
	     * 주말 (토,일)
	     * @param date
	     * @return
	     */
	    public static boolean isWeekend(long date) {
	        boolean result = false;
	        
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(date);
	        
	        //SUNDAY:1 SATURDAY:7
	        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
	            result = true;
	        }
	        
	        return result;
	    }
	    
	    /**
	     * 대체공휴일
	     * @param date
	     * @return
	     */
	    public static boolean isAlternative(long date) {
	        boolean result = false;
	        
	        //설날 연휴와 추석 연휴가 다른 공휴일과 겹치는 경우 그 날 다음의 첫 번째 비공휴일을 공휴일로 하고, 어린이날이 토요일 또는 다른 공휴일과 겹치는 경우 그 날 다음의 첫 번째 비공휴일을 공휴일로 함
	        //1. 어린이날
	        String year = DateFormatUtils.format(date, "yyyy");
	        java.util.Date d = null;
	        try {
	            d = DateUtils.parseDate(year+"0505", "yyyyMMdd");
	        } catch (ParseException e) {}
	        if (isWeekend(d.getTime()) == true) {
	            d = DateUtils.addDays(d, 1);
	        }
	        if (isWeekend(d.getTime()) == true) {
	            d = DateUtils.addDays(d, 1);
	        }
	        if (DateUtils.isSameDay(new java.util.Date(date), d) == true) {
	            result = true;
	        }
	        
	        //2. 설 
	        String lunarDate = getLunarDate(date);
	        Calendar calendar = Calendar.getInstance();
	        d = new java.util.Date(date);
	        if (StringUtils.equals(lunarDate, "0103")) {
	            
	            d = DateUtils.addDays(d, -1);
	            calendar.setTime(d);
	            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	                return true;
	            }
	            
	            d = DateUtils.addDays(d, -1);
	            calendar.setTime(d);
	            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	                return true;
	            }
	            
	            d = DateUtils.addDays(d, -1);
	            calendar.setTime(d);
	            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	                return true;
	            }
	        }
	        
	        //3. 추석
	        d = new java.util.Date(date);
	        if (StringUtils.equals(lunarDate, "0817")) {
	            d = DateUtils.addDays(d, -1);
	            calendar.setTime(d);
	            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	                return true;
	            }
	            
	            d = DateUtils.addDays(d, -1);
	            calendar.setTime(d);
	            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	                return true;
	            }
	            
	            d = DateUtils.addDays(d, -1);
	            calendar.setTime(d);
	            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	                return true;
	            }
	        }
	        
	        return result;
	    }
	    
	}
