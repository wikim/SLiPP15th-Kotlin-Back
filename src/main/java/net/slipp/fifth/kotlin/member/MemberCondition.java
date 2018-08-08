package net.slipp.fifth.kotlin.member;

import java.sql.Date;

import org.springframework.util.StringUtils;

import lombok.Data;

/**
 * 회원 검색조건
 * 
 * @author jiheon
 *
 */
@Data
public class MemberCondition {

    private String signinId;
    private String name;
    private String company;
    private String department;
    private Date createdDate;

    public boolean hasCompany() {
        return StringUtils.hasText(getCompany());
    }

    public boolean hasCreatedDate() {
        return null != createdDate;
    }

    public boolean hasDepartment() {
        return StringUtils.hasText(getDepartment());
    }

    public boolean hasSigninId() {
        return StringUtils.hasText(getSigninId());
    }

    public boolean hasName() {
        return StringUtils.hasText(getName());
    }
}
