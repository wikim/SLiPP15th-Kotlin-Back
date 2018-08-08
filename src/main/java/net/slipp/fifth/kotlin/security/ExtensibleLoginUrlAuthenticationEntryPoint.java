package net.slipp.fifth.kotlin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * XML 헤더를 확인하여 LoginURL로 이동처리
 * 
 * @author jiheon
 *
 */
public class ExtensibleLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    public ExtensibleLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        if (XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.commence(request, response, authException);
        }
    }

}
