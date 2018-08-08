package net.slipp.fifth.kotlin.manager.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.slipp.fifth.kotlin.member.Member;

@Service
public class MemberMenuServiceImpl implements MemberMenuService {

	@Autowired
	private MemberMenuRepository repository;
	
	@Override
	@Transactional
	public MemberMenu save(MemberMenu entity) {
		return repository.save(entity);
	}

	@Override
	public MemberMenu findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public MemberMenu findByMember(Member member) {
		return repository.findByMember(member);
	}

}
