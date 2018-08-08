package net.slipp.fifth.kotlin.manager.menu;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table
public class MenuItem implements Serializable {

	private static final long serialVersionUID = -6790601270034266673L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(length = 255)
	private String menuName;
	@Column(length = 255)
	private String subMenuName;
	@Column(length = 255)
	private String path;

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
