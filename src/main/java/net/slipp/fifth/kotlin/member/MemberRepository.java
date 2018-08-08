package net.slipp.fifth.kotlin.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, QueryDslPredicateExecutor<Member> {

    Member findByName(String name);

    Member findByEmail(String email);

    Member findBySigninId(String signinId);

    Member findById(Long id);

    Member findByNameAndEmail(String name, String email);
}
