package net.slipp.fifth.kotlin.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.slipp.fifth.kotlin.member.Member;

public class SpringSecurityAuditorAware implements AuditorAware<Member> {

    @Override
    public Member getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
            return null;
        }
        return (Member) authentication.getPrincipal();
    }

}
