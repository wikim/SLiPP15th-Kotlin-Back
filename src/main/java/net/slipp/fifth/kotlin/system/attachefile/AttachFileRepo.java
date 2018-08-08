package net.slipp.fifth.kotlin.system.attachefile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachFileRepo
        extends JpaRepository<AttacheFile, Long>, QueryDslPredicateExecutor<AttacheFile> {

    AttacheFile findById(Long id);

}



