package net.slipp.fifth.kotlin.common.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.ToString;

public class PublicStaffUtils {
	private final static int MAX_WEEK = 5;
	
	public static int getFristDay(int size, String dayofweek, int start) {
		int result = 0;
		List<Boolean> checks = null;
		if(size == 2) {
			checks = (start == 0) ? Lists.newArrayList(Boolean.TRUE, Boolean.FALSE) : Lists.newArrayList(Boolean.FALSE, Boolean.TRUE);
			result = getStaffIndex(PublicStaffUtils.getStaffRule(2), dayofweek, checks);
		} else if(size == 3) {
			if(StringUtils.equals(dayofweek, "토")) {
				if(start == 0) {
					checks = Lists.newArrayList(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
				} else if(start == 1) {
					checks = Lists.newArrayList(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
				} else if(start == 2) {
					checks = Lists.newArrayList(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
				}
			} else if(StringUtils.equals(dayofweek, "일")) {
				if(start == 0) {
					checks = Lists.newArrayList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
				} else if(start == 1) {
					checks = Lists.newArrayList(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
				} else if(start == 2) {
					checks = Lists.newArrayList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
				}
			}
			result = getStaffIndex(PublicStaffUtils.getStaffRule(3), dayofweek, checks);
		} else if(size == 4) {
			if(start == 0) {
				checks = Lists.newArrayList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
			} else if(start == 3) {
				checks = Lists.newArrayList(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
			}
			result = getStaffIndex(PublicStaffUtils.getStaffRule(4), dayofweek, checks);
		}
		return result;
	}
				
	public static List<StaffCondtion> getStaffRule(int size) {
		List<StaffCondtion> results = null;
		switch(size) {
			case 1: results = staffOneRule(); break;
			case 2: results = staffTwoRule(); break;
			case 3: results = staffThreeRule(); break;
			case 4: results = staffFourRule(); break;
		}
		return results;
	}
	
	public static StaffCondtion getNextStaffRule(int size, String dayofweek, int pos) {
		StaffCondtion condition = new StaffCondtion();
		List<StaffCondtion> results = null;
		switch(size) {
			case 1: results = staffOneRule(); break;
			case 2: results = staffTwoRule(); break;
			case 3: results = staffThreeRule(); break;
			case 4: results = staffFourRule(); break;
		}
		condition = results.get(pos-1);
		return condition;
	}
	
	private static int getStaffIndex(List<StaffCondtion> list, String dayofweek, List<Boolean> checks) {
		for(int i = 0; i < list.size(); i++) {
			if(StringUtils.equals(list.get(i).getDayofweek(), dayofweek) && list.get(i).getChecks().equals(checks)) {
				return i;
			}
		}
		return 0;
	}
	
	private static List<StaffCondtion> staffOneRule() {
		List<StaffCondtion> results = Lists.newLinkedList();
		StaffCondtion data = new StaffCondtion();
		for(int i = 1; i < MAX_WEEK; i++) {
			if((i % 2) == 1) {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.TRUE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.FALSE));
				results.add(data);
			} else {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.FALSE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.TRUE));
				results.add(data);
			}
		}
		return results;
	}
	
	private static List<StaffCondtion> staffTwoRule() {
		List<StaffCondtion> results = Lists.newLinkedList();
		StaffCondtion data = new StaffCondtion();
		for(int i = 1; i < MAX_WEEK; i++) {
			if((i % 2) == 1) {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.TRUE, Boolean.FALSE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.FALSE, Boolean.TRUE));
				results.add(data);
			} else {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.FALSE, Boolean.TRUE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.TRUE, Boolean.FALSE));
				results.add(data);
			}
		}
		return results;
	}
	
	private static List<StaffCondtion> staffThreeRule() {
		List<StaffCondtion> results = Lists.newLinkedList();
		StaffCondtion data = new StaffCondtion();
		for(int i = 1; i < MAX_WEEK; i++) {
			if((i % 3) == 1) {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
				results.add(data);
			} else if((i % 3) == 2) {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
				results.add(data);
			} else if((i % 3) == 0) {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
				results.add(data);
			}
		}
		return results;
	}
	
	private static List<StaffCondtion> staffFourRule() {
		List<StaffCondtion> results = Lists.newLinkedList();
		StaffCondtion data = new StaffCondtion();
		for(int i = 1; i < MAX_WEEK; i++) {
			if((i % 2) == 1) {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
				results.add(data);
			} else {
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("토");
				data.setChecks(Lists.newArrayList(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
				results.add(data);
				
				data = new StaffCondtion();
				data.setWeek(i);
				data.setDayofweek("일");
				data.setChecks(Lists.newArrayList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
				results.add(data);
			}
		}
		return results;
	}
	
	@Data
	@ToString
	public static class StaffCondtion {
		private Integer week;
		private String dayofweek;
		private List<Boolean> checks = Lists.newLinkedList();
	}
}
