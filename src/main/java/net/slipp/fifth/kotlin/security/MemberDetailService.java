package net.slipp.fifth.kotlin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.member.Member;
import net.slipp.fifth.kotlin.member.MemberRepository;

/**
 * 로그인시 회원정보를 제공하는 SpringSecurity UserDetailService<br/>
 * 구현체 스프링 시큐리티의 요청이 있을 떄 MemberRepository를 사용해서 사용자 정보를 제공한다.
 *
 * @author jiheon
 *
 */
@Slf4j
public class MemberDetailService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberDetailService setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        return this;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("security.exception.empty.username");
        }
        Member member = memberRepository.findBySigninId(username);
        if (null == member) {
            throw new UsernameNotFoundException("Could not found member: " + username);
        }
        
        log.trace("Sign-in member: {}", member.getSigninId());

        return member;
    }
}
