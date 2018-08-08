package net.slipp.fifth.kotlin.service.holyday;

import java.io.Serializable;


import lombok.Data;

@Data
public class HolydayDto implements Serializable {

	private static final long serialVersionUID = -3224491730219999944L;
	
	private Long id;
    private String title;
    private Integer year;
    private String date;
    private String type;
    
    public Holyday bindNewEntity() {
    	Holyday holyday = new Holyday();
        setProperties(holyday);
        return holyday;
    }
    
    public Holyday bind(Holyday holyday) {
    	setProperties(holyday);
        return holyday;
    }
    
   /* public HolydayDto bind(Holyday holyday) {
        setTitle(holyday.getTitle());
        setYear(holyda	y.getYear());
        setDate(holyday.getDate());
        setType(holyday.getType());
        return this;
    }*/

    public HolydayDto createMapper(Holyday target) {
        HolydayDto mapper = new HolydayDto();
        mapper.setId(target.getId());
        mapper.setTitle(target.getTitle());
        mapper.setYear(target.getYear());
        mapper.setDate(target.getDate());
        mapper.setType(target.getType());
        return mapper;
    }

    private void setProperties(Holyday target) {
        target.setId(getId());
        target.setTitle(getTitle());
        target.setYear(getYear());
        target.setDate(getDate());
        target.setType(getType());
    }
}
