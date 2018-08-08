package net.slipp.fifth.kotlin.common;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;


public interface JPQLQueryPredicateExecutor<T> {

    List<T> findAll(JPQLQuery query);
    
    Page<T> findAll(JPQLQuery query, Pageable pageable);
    
    JPQLQuery createQuery(Predicate...predicates);
}
