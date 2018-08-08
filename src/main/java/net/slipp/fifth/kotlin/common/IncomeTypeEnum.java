package net.slipp.fifth.kotlin.common;

import lombok.Getter;

@Getter
public enum IncomeTypeEnum {
	BUY("BUY", "구입"),
	DONATE("DONATE", "기증"),
	DISCARD("DISCARD", "폐기"),
	CHANGE("CHANGE", "관리전환"),
	BUY_DISCARD_SUM("BUY_DISCARD_SUM", "구입+폐기"),
	ALL_SUM("ALL_SUM", "전체합산");
	
	private String key;
	private String value;
	
	private IncomeTypeEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
