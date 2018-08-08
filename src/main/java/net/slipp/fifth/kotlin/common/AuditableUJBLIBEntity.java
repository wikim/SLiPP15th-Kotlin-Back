package net.slipp.fifth.kotlin.common;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import net.slipp.fifth.kotlin.member.Member;

/**
 * 생성자&생성일시, 수정자&수정일시에 관한 정보를 관리하기 위한 처리
 * 
 * @author jiheon.kim
 *
 */
@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class })
public class AuditableUJBLIBEntity extends AbstractUJBLIBAuditable<Member, Long> {
    private static final long serialVersionUID = 359326673134570560L;
}
