package net.slipp.fifth.kotlin.web.support.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.member.Member;
import net.slipp.fifth.kotlin.member.MemberRepository;

/**
 * {@link Controller}에서 인증된 사용자({@link Member})를 인자로 얻을 때 사용하는 HandlerMethodArgumentResolver<br/>
 * 
 * 인증된 회원인 경우에는 회원정보를, 없으면 null을 반환
 * @author jiheon
 *
 */
@Slf4j
public class MemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication && Member.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            member = (Member) authentication.getPrincipal();
            member = memberRepository.findBySigninId(member.getSigninId());
            log.debug("Found SecurityContextHolder-bound authentication: {}", member);
        }
        
        log.debug("No authentication, return null object.");
        return member;
    }

}
