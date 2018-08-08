package net.slipp.fifth.kotlin.common;

import lombok.Getter;

@Getter
public enum RoomEnum {
	NONE("NONE", ""),
	REFERENCE("REFERENCE", "종합"),
	CHILD("CHILD", "어린이실");
	
	private String key;
	private String value;
	
	private RoomEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
