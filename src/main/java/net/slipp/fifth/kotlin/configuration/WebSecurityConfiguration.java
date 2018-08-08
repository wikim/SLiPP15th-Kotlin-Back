package net.slipp.fifth.kotlin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import net.slipp.fifth.kotlin.security.ExtensibleAuthenticationFailureHandler;
import net.slipp.fifth.kotlin.security.ExtensibleLoginUrlAuthenticationEntryPoint;
import net.slipp.fifth.kotlin.security.ExtensibleUserDetailsAuthenticationProvider;
import net.slipp.fifth.kotlin.security.MemberDetailService;
import net.slipp.fifth.kotlin.security.SigninPostAuthenticationChecker;

/**
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/errors/**").permitAll()
                .antMatchers(HttpMethod.GET, "/sign-in", "/sign-up").permitAll()
                .antMatchers(HttpMethod.GET, "/current-date-time", "/current-date-time").permitAll()
                .antMatchers(HttpMethod.POST, "/sign-up").permitAll()
                .antMatchers(HttpMethod.GET, "/find/**").permitAll()
                .antMatchers(HttpMethod.POST, "/find/**").permitAll()
                .antMatchers(HttpMethod.GET, "/members/duplicated-check-email", "/members/duplicated-check-signin-id",
                        "/members/send-password-reset", "/members/confirm-reset-password").permitAll()
                .antMatchers(HttpMethod.PUT, "/members/reset-password").permitAll()
                .antMatchers(HttpMethod.POST, "/settings/password").permitAll()
                .antMatchers("/settings/**").hasAnyAuthority("ADMINISTRATOR", "MANAGER", "OPERATOR")
                .antMatchers(HttpMethod.POST, "/members").hasAnyAuthority("ADMINISTRATOR", "MANAGER", "OPERATOR")
                .antMatchers("/dataroom/**").hasAnyAuthority("ADMINISTRATOR", "MANAGER", "OPERATOR")
                //.antMatchers("/settings/**").hasAnyAuthority("ADMINISTRATOR")
                .antMatchers("/h2console/**").hasAnyAuthority("ADMINISTRATOR")
                .antMatchers("/autoconfig", "/beans", "/configprops", "/dump", "/env", "/metrics", "/mappings", "/trace")
                .authenticated().antMatchers(HttpMethod.POST, "/app_shutdown").hasAnyAuthority("ADMINISTRATOR")
                .antMatchers("/health/check").permitAll()
                .antMatchers("/info", "/health").permitAll().anyRequest().authenticated();


        http.headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "script-src 'self'"))
                .frameOptions().disable();

        http.formLogin().loginPage("/sign-in").loginProcessingUrl("/system/sign-in").usernameParameter("signinId")
                .passwordParameter("password").defaultSuccessUrl("/").successForwardUrl("/").failureUrl("/sign-in?error")
                .failureHandler(new ExtensibleAuthenticationFailureHandler());

        http.logout().invalidateHttpSession(true) // HTTP 세션무효화
                .logoutUrl("/system/sign-out") // SecurityContext 초기화(사용자를 실제로
                                               // 로그아웃시키는 부분)
                .logoutSuccessUrl("/sign-in"); // 로그아웃후 사용자 리다이렉트 URL
        http.httpBasic().authenticationEntryPoint(extensibleLoginUrlAuthenticationEntryPoint());
        http.csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/fonts/**", "/elite/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        ExtensibleUserDetailsAuthenticationProvider authenticationProvider = new ExtensibleUserDetailsAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setSigninPostUserDetailsChecker(userDetailsChecker());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new MemberDetailService();
    }

    @Bean
    public SaltSource saltSource() {
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        return saltSource;
    }

    @Bean
    public UserDetailsChecker userDetailsChecker() {
        return new SigninPostAuthenticationChecker();
    }

    @Bean
    public LoginUrlAuthenticationEntryPoint extensibleLoginUrlAuthenticationEntryPoint() {
    	return new ExtensibleLoginUrlAuthenticationEntryPoint("/sign-in");
    }

   
}
