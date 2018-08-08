package net.slipp.fifth.kotlin.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.member.Member;

/**
 * Sign-in Member checker<br/>
 * 인증중인 사용자 정보를 확인하여 사용가능한지 검증
 *
 * @author jiheon
 *
 */
@Slf4j
public class SigninPostAuthenticationChecker implements UserDetailsChecker {

    /**
     * Check request Authentication Member status.<br/>
     *
     */
    @Override
    public void check(UserDetails toCheck) {
        if (Member.class.isAssignableFrom(toCheck.getClass())) {
            Member member = (Member) toCheck;
            
            if (member.isDisapproval()) {
                log.debug("Member(signinId: {}) status is disapproval.", member.getSigninId());
                throw new BadCredentialsException("system.security.exception.member.status.disapproval");
            }
            if (member.isStop()) {
                log.debug("Member(signinId: {}) don't approval.", member.getSigninId());
                throw new BadCredentialsException("system.security.exception.member.status.stop");
            }
            if(member.isLeave()) {
                throw new BadCredentialsException("system.security.exception.member.status.leave");
            }
        }
    }

}
