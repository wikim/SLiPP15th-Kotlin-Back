package net.slipp.fifth.kotlin.manager.menu;

import net.slipp.fifth.kotlin.member.Member;

public interface MemberMenuService {

	MemberMenu findOne(Long id);
	MemberMenu findByMember(Member member);
	MemberMenu save(MemberMenu entity);
}
