package net.slipp.fifth.kotlin.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.ChineseCalendar;

import net.slipp.fifth.kotlin.service.holyday.Holyday;

public class HolydayList {
	static Logger logger = LoggerFactory.getLogger(HolydayList.class);
	static String[] solar = {"0101", "0301", "0505", "0606", "0815","1003","1009", "1225"};
	static String[] lunar = {"0101", "0102", "0408", "0814", "0815", "0816", "1230"};
	static String[] solarNm = {"신정", "삼일절", "어린이날", "현충일", "광복절","개천절","한글날", "성탄절"};
	static String[] lunarNm = {"설날", "", "부천님오신날", "", "추석", "", ""};
	
	
	
	static boolean holydayLog = false;
	
	@Test
	public void getHolydayListTest() {
		int year = 2018;
		List<Holyday> list = getHolydayList(year);
		if(holydayLog)
			list.forEach(System.out::println);
		
	}
	
	//public static List<String> getHolydayList(int year) {
	public static List<Holyday> getHolydayList(int year) {
		List<Holyday> holydays = new ArrayList<Holyday>();
		
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    cal.set(Calendar.YEAR,year);
		
	     for(int i=1; i<366; i++) {
	    	 
	    	 cal.set(Calendar.DAY_OF_YEAR, i);
	    	 String today = sdf.format(cal.getTime());
	    	 
	    	 try {
	    	
	    		if(isHoliday(today)) {
	    			Holyday h = new Holyday();
	    			h.setDate(today);
	    			h.setTitle(whatHolyDayName(today));
	    			holydays.add(setHolyday(h));
	    		}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	 
	     }
		
		return holydays;
	}
	
    /**
     * 공휴일 여부
     * @param date
     */
    public static boolean isHoliday(String today) throws Exception{
    	
    	long date = DateUtils.parseDate(today, "yyyy-MM-dd").getTime();
        
        boolean isLegalHoliday = isLegalHoliday(date);
        //boolean isWeekend = isWeekend(date);
        boolean isWeekend = false; //토,일 무조건 근무
        boolean isAlternative = isAlternative(date);
        boolean isMonday = isMonday(date);
        boolean isHoliday = isLegalHoliday || isWeekend || isAlternative || isMonday;
        
        if(holydayLog &&  isHoliday) {
        	String logStr = "today : "+today +"/isLegalHoliday:"+isLegalHoliday+"/isWeekend:"+isWeekend+"/isMonday:"+isMonday+"/isAlternative:"+isAlternative+"/isHoliday:"+isHoliday;
	        System.out.println(logStr);
        }
        return isHoliday;
    }
    
    public static String whatHolyDayName(String today) {
    	String nm= "";
    	String day = today.substring(4);
    	day = day.replace("-","");
    	for(int i=0;i<solar.length;i++) {
    		if(solar[i].equals(day)) {
    			return solarNm[i];
    		}
    	}
    	for(int i=0;i<lunar.length;i++) {
    		long date;
			try {
				date = DateUtils.parseDate(today, "yyyy-MM-dd").getTime();
				if(lunar[i].equals( getLunarDate(date)) ) {
	    			return lunarNm[i];
	    		}
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		
    	}
    	
    	return nm;
    }
    
    public static Holyday setHolyday(Holyday h) throws ParseException {
    	String today = h.getDate();
    	long date = DateUtils.parseDate(today, "yyyy-MM-dd").getTime();
        
        boolean isLegalHoliday = isLegalHoliday(date);
        boolean isWeekend = isWeekend(date);
        boolean isAlternative = isAlternative(date);
        boolean isMonday = isMonday(date);
        
        if(isLegalHoliday) h.setType("LEGAL");
        else if(isWeekend) h.setType("WEEK");
        else if(isAlternative) h.setType("ALTERNATIVE");
        else if(isMonday) h.setType("WEEK");
        else {
        	System.out.println("xxxxxxxxxx");
        }
        return h;
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
        
        List<String> solarList = Arrays.asList(solar);
        List<String> lunarList = Arrays.asList(lunar);
        
        String solarDate = DateFormatUtils.format(date, "MMdd");
        ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));
        
//        String y = String.valueOf(cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637);
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
     * 휴관일 (월)
     * @param date
     * @return
     */
    public static boolean isMonday(long date) {
        boolean result = false;
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        
        //SUNDAY:1 SATURDAY:7
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.MONDAY ) {
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
            return true;
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
        if (StringUtils.equals(lunarDate, "0817")) {
        	d = new java.util.Date(date);
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
