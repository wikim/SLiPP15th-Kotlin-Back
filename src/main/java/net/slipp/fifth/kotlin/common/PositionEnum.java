package net.slipp.fifth.kotlin.common;

import lombok.Getter;

@Getter
public enum PositionEnum {
	NONE("NONE", ""),
	INFOMATION("INFOMATION", "문헌정보과장"),
	LIBRARY("LIBRARY", "학교도서관지원과장"),
	OFFICER("OFFICER", "주무관"),
	PUBLIC("PUBLIC", "공익");
	
	private String key;
	private String value;
	
	private PositionEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
