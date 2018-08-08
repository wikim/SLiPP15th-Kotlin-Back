package net.slipp.fifth.kotlin.manager.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.slipp.fifth.kotlin.member.Member;

@Repository
public interface MemberMenuRepository extends JpaRepository<MemberMenu, Long> {

	MemberMenu findByMember(Member member);
}
