package net.slipp.fifth.kotlin.common;

import lombok.Getter;

@Getter
public enum WorkDayEnum {
	SATURDAY("SATURDAY", "토"),
	SUNDAY("SUNDAY", "일"),
	WEEKDAY("WEEKDAY", "평일");
	
	private String key;
	private String value;
	
	private WorkDayEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
