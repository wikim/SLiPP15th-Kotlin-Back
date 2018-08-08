package net.slipp.fifth.kotlin.service.holyday;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import net.slipp.fifth.kotlin.common.JPQLQueryPredicateExecutor;

@Repository
public interface HolydayRepository extends JpaRepository<Holyday, Long>, QueryDslPredicateExecutor<Holyday>,
        JPQLQueryPredicateExecutor<Holyday> {
	
	List<Holyday> findByYear(int year);
	
}
