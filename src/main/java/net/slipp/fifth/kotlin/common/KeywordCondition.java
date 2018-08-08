package net.slipp.fifth.kotlin.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class KeywordCondition {

	private String pYear;
	private String pDate;
	private List<String> pYears;
	
    private String keyword;
    
    public KeywordCondition(){
    	pYears = new ArrayList<String>();
    }
    public void setPYears(String v) {
    	pYears.add(v);
    }
    public boolean hasKeyword() {
        return this.keyword != null;
    }
    
    
}
