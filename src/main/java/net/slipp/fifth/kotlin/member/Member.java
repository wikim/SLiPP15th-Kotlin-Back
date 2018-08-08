package net.slipp.fifth.kotlin.member;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.slipp.fifth.kotlin.manager.menu.MemberMenu;
import net.slipp.fifth.kotlin.member.typ.MemberAuthority;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFile;


@Data
@Entity
@NoArgsConstructor
@EntityListeners(MemberEntityListener.class)
@Table(indexes = { 	@Index(name = "idx_member_id", columnList = "id"),
					@Index(name = "idx_member_status", columnList = "status"),
        			@Index(name = "idx_member_created_date", columnList = "createdDate") 
				 })
@ToString(exclude = { "password" })
@EqualsAndHashCode(of = { "signinId", "email" })
public class Member implements Serializable, UserDetails {
    private static final long serialVersionUID = -6030379152049260140L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "member.required.signinId")
    @Column(length = 50, unique = true, nullable = false)
    private String signinId;

    @NotEmpty(message = "member.required.password")
    @Column(length = 100, nullable = false)
    private String password;
    
    @ElementCollection(targetClass = MemberAuthority.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Set<MemberAuthority> authorities = new HashSet<>();


    @NotEmpty(message = "member.required.email")
    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "member.required.name")
    @Column(length = 50, nullable = false)
    private String name;
	
    @Column(length = 50)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(length = 100) private String departmentNm;
    @Column(length = 100) private String gradeNm; //  직책  : 팀장, 과장, 주무관, 공익 
    @Column(length = 100) private String chargeRoom; //종합자료실 , 어린이실, 기타  
    @Column(length = 100) private String  cellPhoneNumber; 
    @Column(length = 100) private String  inPhoneNumber; 
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberMenu> menus;
    
    /**
     * 생성자
     */
    @ManyToOne
    private Member createdBy;
    /**
     * 생성일시
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    /**
     * 수정자
     */
    @ManyToOne
    private Member lastModifiedBy;
    /**
     * 수정일시
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    /**
     * Profile image
     */
    @OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
    private AttacheFile profileImage;

    
    public Member(String signinId, String email, String name, String password) {

        this.signinId = signinId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.status = MemberStatus.ACTIVE;
        this.authorities = Sets.newHashSet();
    }
    @PrePersist
    protected void onCreated() {
        this.createdDate = DateTime.now().toDate();
    }

    @PreUpdate
    protected void onModified() {
        this.lastModifiedDate = DateTime.now().toDate();
    }


    public Member setName(String name) throws IllegalArgumentException {
        Assert.hasText(name, "member.exception.hasText.name");
        this.name = name;
        return this;
    }

    /**
     * 회원 이름변경
     *
     * @param name
     * @return
     * @throws IllegalArgumentException
     *             member.exception.hasText.name
     */
    public Member changeName(String name) throws IllegalArgumentException {
        setName(name);
        return this;
    }


    /**
     * 회원 권한변경
     *
     * @param authority
     * @return this
     */
    public Member changeAuthorities(Set<MemberAuthority> authorities) {
    	Assert.notEmpty(authorities, "authorities is not null");
        this.authorities = authorities;
        return this;
    }

    /**
     * Change Member's password
     *
     * @param password
     * @return {@link Member} changed password.
     */
    public Member changePassword(String password) {
        Assert.hasText(password, "member.exception.hasText.password");
        this.password = password;
        return this;
    }

    /**
     * @return true: id is null / false: id is not null.
     */
    public boolean isNew() {
        return null == getId();
    }

    /**
     * Member status is Active?
     *
     * @return true: yes / false: no.
     */
    public boolean isActive() {
        return getStatus() == MemberStatus.ACTIVE;
    }

    /**
     * Member status is Stop?
     *
     * @return true: yes / false: no.
     */
    public boolean isStop() {
        return getStatus() == MemberStatus.STOP;
    }

    /**
     * Member.status is Leave?
     *
     * @return true: yes / false: no
     */
    public boolean isLeave() {
        return getStatus() == MemberStatus.LEAVE;
    }

    /**
     * Change member.status to Active.
     *
     * @return Changed {@link Member} status to {@link MemberStatus.ACTIVE}
     *
     */
    public Member active() {
        this.status = MemberStatus.ACTIVE;
        return this;
    }

    /**
     * Change member.status to stop.
     *
     * @return Changed {@link Member} status to {@link MemberStatus.STOP}
     *
     */
    public Member stop() {
        this.status = MemberStatus.STOP;
        return this;
    }

    /**
     * Change member.status to Leave.
     *
     * @return Changed {@link Member} status to {@link MemberStatus.LEAVE}
     *
     */
    public Member leave() {
        this.status = MemberStatus.LEAVE;
        return this;
    }

    /**
     * Change member.status to DISAPPROVAL.
     *
     * @return Changed {@link Member} status to {@link MemberStatus.DISAPPROVAL}
     *
     */
    public Member disapproval() {
        this.status = MemberStatus.DISAPPROVAL;
        return this;
    }
    
    @Override
    public String getUsername() {
        return getSigninId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return MemberStatus.LEAVE != getStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;

	}
    
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * 사용자권한을 가지고 있는지 확인
     *
     * @param authority
     *            {@link MemberAuthority}
     * @return true: yes/ no: false
     */
    public boolean hasAuthority(MemberAuthority authority) {
        return null != getAuthorities() && getAuthorities().contains(authority);
    }

    public String getProfileImageDirectoryPath() {
        return Paths.get(File.separator, "profile", getId().toString()).toString();
    }

    public boolean isDisapproval() {
        return MemberStatus.DISAPPROVAL == getStatus();
    }


}
