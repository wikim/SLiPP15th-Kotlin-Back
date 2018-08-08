package net.slipp.fifth.kotlin.dataroom;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.KeywordCondition;
import net.slipp.fifth.kotlin.common.mapper.ExtensibleModelMapper;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.common.util.DateUtil;
import net.slipp.fifth.kotlin.dataroom.domain.WorkDay;
import net.slipp.fifth.kotlin.dataroom.volunteer.VolunteerLog;
import net.slipp.fifth.kotlin.dataroom.volunteer.VolunteerLogService;
import net.slipp.fifth.kotlin.member.MemberService;
import net.slipp.fifth.kotlin.service.holyday.Holyday;
import net.slipp.fifth.kotlin.service.holyday.HolydayService;

@Slf4j
@Controller
@RequestMapping("/dataroom")
public class DataRoomController {
    private static final int FIRST_LEGACY_YEAR = 2006;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private VolunteerLogService volunteerLogService;

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private HolydayService holydayService;
    
    private ExtensibleModelMapper extensibleModelMapper = new ExtensibleModelMapper();

    @PostConstruct
    private void setUp() {
        extensibleModelMapper.setModelMapper(modelMapper);
    }

    /**
     * 
     * @param model
     * @param pDt
     * @param condition
     * @param pageStatus
     * @return
     */
    @RequestMapping(value = {"/volunteerLog","/volunteerLog/{pDt}"}, method = RequestMethod.GET)
    public String getVolunteerList(Model model
    		, @PathVariable Optional<String > pDt
    		, KeywordCondition condition, PageStatus pageStatus ) {
    	
    	KeywordCondition dfKey = DateUtil.setDate(pDt);
		condition.setPYear(dfKey.getPYear() );
		model.addAttribute("pYears", dfKey.getPYears());
		model.addAttribute("pYear", dfKey.getPYear());
		model.addAttribute("pDate", dfKey.getPDate());
    	condition.setKeyword(dfKey.getPYear());
    	
    	 
         	List<Holyday> holydays = holydayService.findHolydaysByYear(Integer.parseInt( dfKey.getPYear() ));
         	List<VolunteerLog> volunteerLog = volunteerLogService.findVolunteerLogByPYear(dfKey.getPYear());
         	
         	List<WorkDay> workDays = new ArrayList<WorkDay>();
         	for(VolunteerLog c:volunteerLog) {
         		WorkDay  w = new WorkDay();
         		w.setYear(Integer.parseInt(c.getPYear()));
         		w.setDate(c.getPDate());
         		w.setTitle("");
         		w.setType("WORK");
         		workDays.add(w);
         	}
         	
         	for(Holyday c:holydays) {
         		WorkDay  w = new WorkDay();
         		w.setYear(c.getYear());
         		w.setDate(c.getStart());
         		w.setTitle(c.getTitle());
         		w.setColor(c.getColor());
         		w.setType(c.getType());
         		workDays.add(w);
         	}
         	
         	ObjectMapper mapper = new ObjectMapper();
         	
         	try {
				model.addAttribute("holydays", mapper.writeValueAsString(holydays));
				model.addAttribute("workDays", mapper.writeValueAsString(workDays));    	
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
 			return "yo/dataroom/volunteerLogList";
    }
    
    @RequestMapping(value = {"/volunteerLog/create/{pDate}" }, method = RequestMethod.GET)
    public String createVolunteerLog(Model model,@PathVariable String pDate,  KeywordCondition condition, PageStatus pageStatus) {
    	
    	VolunteerLog volunteerLog = new VolunteerLog();
    	volunteerLog.setPYear(pDate.substring(0,  pDate.indexOf("-")));
    	volunteerLog.setPDate(pDate);
    	
    	model.addAttribute("openDays", getOpenDays(pDate.substring(0,pDate.indexOf("-")), pDate));
		model.addAttribute("volunteerLog",volunteerLog);
		
        return "yo/dataroom/volunteerLog";
    }
    
    @RequestMapping(value = {"/volunteerLog/view/{pDate}" }, method = RequestMethod.GET)
    public String viewVolunteer(Model model,@PathVariable String pDate,  KeywordCondition condition, PageStatus pageStatus) {
    	VolunteerLog volunteerLog = new VolunteerLog();
		volunteerLog = volunteerLogService.findByPDate(pDate);
		if(null == volunteerLog || volunteerLog.isNew()) 
			return "redirect:/dataroom/volunteerLog/create/"+pDate;
		else 
			return "redirect:/dataroom/volunteerLog/modify/"+volunteerLog.getId();
    }
    
    @RequestMapping(value = "/volunteerLog/create", method = RequestMethod.PUT)
    public ResponseEntity<HashMap<String, String>> setDailVolunteerLog(Model model, @Valid VolunteerLog request , KeywordCondition condition, PageStatus pageStatus) {
    	VolunteerLog volunteerLog = volunteerLogService.save(request);
    	
    	model.addAttribute("volunteerLog", volunteerLog);
    	
    	HashMap<String, String> requestMap = new HashMap<String, String>();
    	requestMap.put("id", volunteerLog.getId()+"");
    	requestMap.put("pDate", volunteerLog.getPDate());
    	
        return ResponseEntity.status(HttpStatus.OK).body(requestMap);
    }
    
    @RequestMapping(value = {"/volunteerLog/modify/{id}" }, method = RequestMethod.GET)
    public String modifyVolunteer(Model model,@PathVariable Long id,  KeywordCondition condition, PageStatus pageStatus) {
    	VolunteerLog volunteerLog = volunteerLogService.findOne(id);
    	model.addAttribute("openDays", getOpenDays(volunteerLog.getPYear(), volunteerLog.getPDate()));
		model.addAttribute("volunteerLog",volunteerLog);
        return "yo/dataroom/volunteerLog";
    }
    
    @RequestMapping(value = {"/volunteerLog/delete/" }, method = RequestMethod.PUT)
    public ResponseEntity<HashMap<String, String>> deleteVolunteerLog(Model model,VolunteerLog  volunteerLog ) {
    	volunteerLogService.deleteById(volunteerLog.getId());
    	HashMap<String, String> requestMap = new HashMap<String, String>();
        return ResponseEntity.status(HttpStatus.OK).body(requestMap);
    }
    
    private int getOpenDays(String pYearStr, String toDt) {
    	
    	if(toDt.indexOf("-01-01")>0) return 0;
    	//선택 일자 - 올해년도 - 휴관일
		int pYear = Integer.parseInt(pYearStr);
		String fromDt = pYear+"-01-01"; 
		long diffDay =  DateUtil.diffDateDay(fromDt, toDt)+1; //총일수 : 당일도 하루로 계
		List<Holyday> holydays = holydayService.findHolydaysByYear(pYear);
		int hDiff = 0;
		for(Holyday h : holydays) {
			int compare=0;
			try {
				compare = DateUtil.strFmtToDate(h.getDate(),"yyyy-MM-dd").compareTo(DateUtil.strFmtToDate(toDt, "yyyy-MM-dd"));
				//현재일보다 작을때까지 
				if(compare <= 0) {
					hDiff++;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return (int)diffDay - hDiff;
    }
    

}