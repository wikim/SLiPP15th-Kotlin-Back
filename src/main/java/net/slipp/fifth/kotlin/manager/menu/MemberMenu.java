package net.slipp.fifth.kotlin.manager.menu;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import lombok.Data;
import lombok.ToString;
import net.slipp.fifth.kotlin.member.Member;

@Data
@Entity
@ToString(exclude = { "member" })
@Table(indexes = { 	
		@Index(name = "idx_member_menu_menu_id", columnList = "menuId"),
		@Index(name = "idx_member_menu_created_date", columnList = "createdDate") 
	 })
public class MemberMenu implements Serializable {

	private static final long serialVersionUID = -6790601270034266673L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member member;

	@Column(nullable = false)
	private Long menuId;

	@Column(columnDefinition="tinyint(1) default 0")
	private boolean readYn;
	@Column(columnDefinition="tinyint(1) default 0")
	private boolean printYn;
	@Column(columnDefinition="tinyint(1) default 0")
	private boolean createYn;
	@Column(columnDefinition="tinyint(1) default 0")
	private boolean updateYn;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @PrePersist
    protected void onCreated() {
        this.createdDate = DateTime.now().toDate();
    }
    @PreUpdate
    protected void onModified() {
        this.lastModifiedDate = DateTime.now().toDate();
    }
}
