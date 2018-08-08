package net.slipp.fifth.kotlin.service.holyday;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(indexes = { @Index(name = "idx_holyday_id", columnList = "id")})
public class Holyday {
	
	@Id                                                                                                                    
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String title;
	
	@Column(length = 4)
	private int year;
	
	@Column(length = 20)
	private String date; // yyyy-mm-dd
	
	@Column(length = 20)
	private String type; // PUBLIC:법정공휴일, SAT:주말(토),SUN:주말(일),CUTOM:커스텀휴관일 
	
	@Transient
	private String start;
	
	@Transient
	private boolean allday=true;
	
	@Transient
	private String color;
	
	public void setStart(String date) {
		this.start = this.date;
	}
	public String getStart() {
		return this.date;
	}
	
	
	public void setColor() {
		
	}
	public String getColor() {
		if(null != this.type) {
		 if(this.type.equals("LEGAL")) return "#00c292"; //green
		 else if(this.type.equals("WEEK")) return "#e46a76"; //red
		 else if(this.type.equals("ALTERNATIVE")) return "#e46a76";//red
		 else if(this.type.equals("CUSTOM")) return "#fec107"; //yellow
		}
		return "#e46a76";
	}
	

	public HolydayDto createDto() {
		HolydayDto dto = new HolydayDto();
        dto.setId(getId());      
        dto.setTitle(getTitle());
        dto.setYear(getYear());  
        dto.setDate(getDate());  
        dto.setType(getType());  
        return dto;
	}
	
	
	
}
