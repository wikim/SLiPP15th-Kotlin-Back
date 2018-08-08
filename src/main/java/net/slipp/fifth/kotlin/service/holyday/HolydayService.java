package net.slipp.fifth.kotlin.service.holyday;

import java.util.List;


public interface HolydayService {


    Holyday findOne(Long id);

    List<Holyday> findHolydaysByYear(int year);
    
    void deleteYear(Holyday holyday);
    
    void delete(Holyday holyday);
    
    List<Holyday> reset(int year);
    
    Holyday save(Holyday job);
    
    

}
