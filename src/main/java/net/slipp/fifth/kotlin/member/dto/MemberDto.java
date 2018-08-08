package net.slipp.fifth.kotlin.member.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.slipp.fifth.kotlin.member.MemberStatus;
import net.slipp.fifth.kotlin.member.typ.MemberAuthority;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFileDto;

@Data
public class MemberDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotEmpty
    @Length(max = 50)
    private String signinId;
    @Email
    @NotEmpty
    @Length(max = 100)
    private String email;
    @NotEmpty
    @Length(max = 50)
    private String name;
    @Length(max = 50)
    private String nickName;

    private MemberStatus status;

    @Length(max = 20)
    private String cellPhoneNumber;

    @Length(max = 100)
    private String company;

    @Length(max = 100)
    private String department;

    private String note;
    
     @Length(max = 100) private String departmentNm;
     @Length(max = 100) private String gradeNm; //  직책  : 팀장, 과장, 주무관, 공익 
     @Length(max = 100) private String chargeRoom; //종합자료실 , 어린이실, 기타  
     @Length(max = 100) private String inPhoneNumber; 

    private Set<MemberAuthority> authorities;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString(exclude = { "password" }, callSuper = true)
    public static class Request extends MemberDto {
        private static final long serialVersionUID = 1L;
        @Length(max = 100)
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Response extends MemberDto {
        private static final long serialVersionUID = 1L;

        private MemberDto createdBy;
        private Date createdDate;
        private MemberDto lastModifiedBy;
        private Date lastModifiedDate;
    }

    private boolean hasAuthority(final MemberAuthority targetAuthority) {
        return Iterables.any(this.authorities, new Predicate<MemberAuthority>() {
            @Override
            public boolean apply(MemberAuthority input) {
                return input == targetAuthority;
            }
        });
    }

    public boolean hasAdministratorAuthority() {
        return hasAuthority(MemberAuthority.ADMINISTRATOR);
    }

    public boolean hasMoreThanProjectManagerAuthority() {
        return hasAuthority(MemberAuthority.ADMINISTRATOR) || hasAuthority(MemberAuthority.MANAGER);
    }

    public boolean hasMoreThanOperatorAuthority() {
        return hasAuthority(MemberAuthority.ADMINISTRATOR) || hasAuthority(MemberAuthority.MANAGER)
                || hasAuthority(MemberAuthority.OPERATOR);
    }
    

}
