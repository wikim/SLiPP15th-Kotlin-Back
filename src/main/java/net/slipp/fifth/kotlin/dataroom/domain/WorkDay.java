package net.slipp.fifth.kotlin.dataroom.domain;

import lombok.Data;

@Data
public class WorkDay {
	
	private Long id;
    private String title;
    private Integer year;
    private String date;
    private String type;
    private String color;
    private boolean allday=true;
	
	
}