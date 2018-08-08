package net.slipp.fifth.kotlin.dataroom.volunteer;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;

import net.slipp.fifth.kotlin.common.KeywordCondition;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;


public interface VolunteerLogService {


	VolunteerLog findOne(Long id);

	VolunteerLog findByWDate(String date) throws ParseException;
	
	VolunteerLog findByPDate(String date);
    
	VolunteerLog selectSumByYear(String year);
    
	VolunteerLog save(VolunteerLog pdsLog);
	
    public Page<VolunteerLog> search(KeywordCondition condition, PageStatus pageStatus);

    public List<VolunteerLog> findByPDateBetween(String startDt, String endDt);
    
    public VolunteerLog selectSumByPDateBetween(String fromDt, String toDt);
    
    public List<VolunteerLog> findVolunteerLogByPYear(String year);
    
    void deleteById(Long id);


}
