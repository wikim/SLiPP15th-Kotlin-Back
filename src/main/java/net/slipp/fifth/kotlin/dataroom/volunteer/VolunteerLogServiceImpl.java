package net.slipp.fifth.kotlin.dataroom.volunteer;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.KeywordCondition;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.common.util.DateUtil;

@Slf4j
@Service
public class VolunteerLogServiceImpl implements VolunteerLogService {
	
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private VolunteerLogRepository volunteerLogRepository;

	@Override
	public VolunteerLog findOne(Long id) {
		return volunteerLogRepository.findOne(id);
	}

	@Override
	public VolunteerLog findByWDate(String date) throws ParseException {
		
		return volunteerLogRepository.findByWDate(DateUtil.strToDate(date, "yyyy-MM-dd"));
	}

	@Override
	public VolunteerLog save(VolunteerLog pdsLog)  {
		try {
			pdsLog.setWDate( DateUtil.strToDate(pdsLog.getPDate(), "yyyy-MM-dd"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return volunteerLogRepository.save(pdsLog);
	}

	@Override
	public VolunteerLog selectSumByYear(String date) {
		StringBuffer str = new StringBuffer("SELECT ");
		str.append("SUM( UserInMan ) AS UserInManSum, SUM( UserInWoman ) AS UserInWomanSum,SUM( UserInMan + UserInWoman ) AS UserInSum") 
		.append(" FROM  PdsLog WHERE TO_CHAR(wDate,'YYYYMMDD') <= ?1 GROUP BY TO_CHAR(wDate,'YYYY') ");
		
		Query query = entityManager.createQuery(str.toString());
		query.setParameter(1, date);
		List<Object[]> result = query.getResultList();
		VolunteerLog pdsLog = new VolunteerLog();
		for(Object[] o : result) {
			
			/*//이용자현황 
			pdsLog.setUserInManSum(Integer.parseInt(o[0].toString()));
			pdsLog.setUserInWomanSum(Integer.parseInt(o[1].toString()));
			pdsLog.setUserInSum(Integer.parseInt(o[2].toString()));
			pdsLog.setUserOutManSum(Integer.parseInt(o[3].toString()));
			pdsLog.setUserOutWomanSum(Integer.parseInt(o[4].toString()));
			pdsLog.setUserOutSum(Integer.parseInt(o[5].toString()));
			pdsLog.setUserManSum(Integer.parseInt(o[6].toString()));
			pdsLog.setUserWomanSum(Integer.parseInt(o[7].toString()));
			pdsLog.setUserSum(Integer.parseInt(o[8].toString()));*/
			
			//이용자료 현황 관내 
			/*List<Integer> UseInSums = new ArrayList<Integer>();
			for(int i=9; i<=18; i++) { //9~18
				UseInSums.add(Integer.parseInt(o[i].toString()));
			}
			pdsLog.setUseInSums(UseInSums);*/
			
			//이용자료 현황 관외  
			/*List<Integer> UseOutSums = new ArrayList<Integer>();
			for(int i=19; i<=28; i++) { //19~28
				UseOutSums.add(Integer.parseInt(o[i].toString()));
			}
			pdsLog.setUseOutSums(UseOutSums);*/
			
		}
		/*if(result.size() ==0) {
			List<Integer> UseDummySums = new ArrayList<Integer>();
			for(int i=9; i<=18; i++) { //9~18
				UseDummySums.add(0);
			}
			pdsLog.setUseInSums(UseDummySums);
			pdsLog.setUseOutSums(UseDummySums);
		}*/
		
		return pdsLog;
	}
	
	@Override
    public Page<VolunteerLog> search(KeywordCondition condition, PageStatus pageStatus) {
        QVolunteerLog volunteerLog = QVolunteerLog.volunteerLog;

        BooleanBuilder predicate = new BooleanBuilder();
        if (condition.hasKeyword()) {
            String likeKeyword = new StringBuilder().append("%").append(condition.getKeyword()).append("%").toString();
            predicate.and(volunteerLog.pYear.like(likeKeyword));
        }

        Page<VolunteerLog> page = volunteerLogRepository.findAll(predicate, pageStatus);
        return page;
    }

	@Override
	public VolunteerLog findByPDate(String date)  {
		return volunteerLogRepository.findByPDate(date);

	}

	@Override
	public List<VolunteerLog> findByPDateBetween(String startDt, String endDt) {
		return volunteerLogRepository.findByPDateBetween(startDt, endDt);
	}

	@Override
	public VolunteerLog selectSumByPDateBetween(String fromDt, String toDt) {
		StringBuffer str = new StringBuffer("SELECT ");
		str.append("SUM(User_Youth_Boy_Volunteer) AS UserYouthBoyVolunteer, SUM(User_Youth_Girl_Volunteer) AS UserYouthGirlVolunteer , SUM(User_Man_Volunteer) AS UserManVolunteer, SUM(User_Woman_Volunteer) AS UserWomanVolunteer ")  
		.append("FROM Volunteer_Log ")
		.append("WHERE p_Date BETWEEN :fromDt and :toDt  ");
		
		Query query = entityManager.createNativeQuery(str.toString());
		query.setParameter("fromDt", fromDt);
		query.setParameter("toDt", toDt);
		List<Object[]> result = query.getResultList();
		VolunteerLog pLog = new VolunteerLog();
		for(Object[] o : result) {
			if(o[0] == null) continue;
			pLog.setUserYouthBoyVolunteer(Integer.parseInt(o[0].toString()));
			pLog.setUserYouthGirlVolunteer(Integer.parseInt(o[1].toString()));
			pLog.setUserManVolunteer(Integer.parseInt(o[2].toString()));
			pLog.setUserWomanVolunteer(Integer.parseInt(o[3].toString()));
		}
		
		return pLog;
	}
   
	@Override
	public List<VolunteerLog> findVolunteerLogByPYear(String year) {
		return volunteerLogRepository.findByPYear(year);
	}

	@Override
	public void deleteById(Long id) {
		volunteerLogRepository.delete(id);
	}
}
