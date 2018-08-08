package net.slipp.fifth.kotlin.service.holyday;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.util.HolydayList;

@Slf4j
@Service
public class HolydayServiceImpl implements HolydayService {
	
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private HolydayRepository holydayRepository;

    
    @Override
	public List<Holyday> reset(int year) {
    	
    	//공휴일 리스트 
    	List<Holyday> holdayDates = HolydayList.getHolydayList(year);
        
    	//해당년도의 휴일 삭제 
    	Holyday holyday = new Holyday();
    	holyday.setYear(year);
    	holydayRepository.deleteInBatch(holydayRepository.findByYear(year));
    	
    	List<Holyday> holydays = new ArrayList<Holyday>();
    	
    	Iterator<Holyday> it = holdayDates.iterator();	
    	while (it.hasNext()) {
    		Holyday h = it.next();
    		h.setYear(year);
    		holyday.setDate(h.getDate()+" 00:00:00");
    		holyday.setType(h.getType());
    			
			if(h.getType().equals("LEGAL")) h.setColor("#00c292");
			else if(h.getType().equals("WEEK")) h.setColor("#e46a76");
		    else if(h.getType().equals("ALTERNATIVE")) h.setColor("#fec107");
		    else if(h.getType().equals("CUSTOM")) h.setColor("#fec107");
		
    		holydayRepository.save(h);
    		holydays.add(h);
    	}
    	
        return holydays;
	}

    
    
	@Override
	public Holyday findOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public Holyday save(Holyday holyday) {
        return holydayRepository.save(holyday);
	}

	@Override
	public void deleteYear(Holyday holyday) {
       holydayRepository.delete(holyday);
	}



	@Override
	public List<Holyday> findHolydaysByYear(int year) {
		return holydayRepository.findByYear(year);
	}

	@Override
	public  void delete(Holyday holyday) {
		holydayRepository.delete(holyday);
		holydayRepository.flush();
	}
	
}
