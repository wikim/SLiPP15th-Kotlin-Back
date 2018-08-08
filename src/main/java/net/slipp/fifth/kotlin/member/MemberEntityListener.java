package net.slipp.fifth.kotlin.member;

import javax.annotation.Resource;
import javax.persistence.PostRemove;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.AutowireHelper;
import net.slipp.fifth.kotlin.configuration.ApplicationProperties;

@Slf4j
@Component
public class MemberEntityListener {
    @Resource
    private ApplicationProperties ujblibProperties;

    @PostRemove
    public void postRemove(Member member) {
        AutowireHelper.autowire(this, this.ujblibProperties);

    }
}
