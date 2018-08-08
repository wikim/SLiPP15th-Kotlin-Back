package net.slipp.fifth.kotlin.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


import lombok.extern.slf4j.Slf4j;

/**
 * SpringSecurity에서 DB 회원정보를 조회하는 기능구현
 * {@link AbstractUserDetailsAuthenticationProvider} 확장<br/>
 * {@link DaoAuthenticationProvider}에서 사용하는
 * {@link org.springframework.security.authentication.encoding.PasswordEncoder}가
 * Deprecated 되어 이를 대체할 {@link PasswordEncoder}를 적용하기 위해서
 * 
 * @author jiheon
 *
 */
@Slf4j
public class ExtensibleUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements
        InitializingBean {
	
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private UserDetailsChecker signinPostUserDetailsChecker;

    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public UserDetailsChecker getSigninUserDetailsChecker() {
        return signinPostUserDetailsChecker;
    }

    public void setSigninPostUserDetailsChecker(UserDetailsChecker signinUserDetailsChecker) {
        this.signinPostUserDetailsChecker = signinUserDetailsChecker;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    	
        if (null == authentication.getCredentials()) {
            logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException("security.exception.noCredentials");
        }

        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");
            throw new BadCredentialsException("security.exception.badCredentials");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UserDetails loadedUser;

        try {
            log.trace("retrieveUser : {}", username);
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        } catch (UsernameNotFoundException notFound) {
            if (hideUserNotFoundExceptions) {
                throw new BadCredentialsException("security.exception.badCredentials");
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (null == loadedUser) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }

        signinPostUserDetailsChecker.check(loadedUser);
        
        return loadedUser;
    }
}
