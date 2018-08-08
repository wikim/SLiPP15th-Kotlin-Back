package net.slipp.fifth.kotlin.dataroom.volunteer;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.slipp.fifth.kotlin.common.AuditableUJBLIBEntity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = { "wDate" }, callSuper = false)
@Table(indexes = { @Index(name = "idx_volunteerlog_id", columnList = "id")})
public class VolunteerLog extends AuditableUJBLIBEntity implements Serializable{


	private static final long serialVersionUID = 2402090854684465948L;

	@Id                                                                                                                    
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date wDate;
	
	@Column(length = 10)
	private String pYear="";
	
	@Column(length = 10)
	private String pDate="";
	
	@Column(length = 10)
	private int UserYouthBoyVolunteer;
	
	@Column(length = 10)
	private int UserYouthGirlVolunteer;
	
	@Column(length = 10)
	private int UserManVolunteer;
	
	@Column(length = 10)
	private int UserWomanVolunteer;

	@Column(length = 1000)
	private String EtcInform;
	
	/** **/
	
	@Transient
	private String cDate; //wDate.format("YYYY-MM-DD")

	
	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}
	
}
