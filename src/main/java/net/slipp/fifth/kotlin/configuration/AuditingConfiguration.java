package net.slipp.fifth.kotlin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import net.slipp.fifth.kotlin.security.SpringSecurityAuditorAware;

/**
 * AuditableEntity를 확장한 클래스에 생성자, 최근수정자 정보를 주입한다.
 * 
 * 스프링시큐리티를 통해서 인증받은 사용자정보가 주입된다.
 * 
 * @author jiheon
 *
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {
    @Bean
    SpringSecurityAuditorAware auditorAware() {
        return new SpringSecurityAuditorAware();
    }
}
