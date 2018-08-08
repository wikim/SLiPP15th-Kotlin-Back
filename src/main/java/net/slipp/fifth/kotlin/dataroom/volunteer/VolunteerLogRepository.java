package net.slipp.fifth.kotlin.dataroom.volunteer;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import net.slipp.fifth.kotlin.common.JPQLQueryPredicateExecutor;

@Repository
public interface VolunteerLogRepository extends JpaRepository<VolunteerLog, Long>, QueryDslPredicateExecutor<VolunteerLog>,
        JPQLQueryPredicateExecutor<VolunteerLog> {
	
	public VolunteerLog findByWDate(Date wDate);
	
	public VolunteerLog findByPDate(String pDate);
	
	public List<VolunteerLog> findByPDateBetween(String startDate, String endDate);

	List<VolunteerLog> findByPYear(String pYear);

}
