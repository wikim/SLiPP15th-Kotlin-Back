package net.slipp.fifth.kotlin.manager;

import java.util.Arrays;
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

import net.slipp.fifth.kotlin.common.KeywordCondition;
import net.slipp.fifth.kotlin.common.mapper.ExtensibleModelMapper;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.common.util.DateUtil;
import net.slipp.fifth.kotlin.service.holyday.Holyday;
import net.slipp.fifth.kotlin.service.holyday.HolydayService;

@Controller
@RequestMapping("/settings")
public class HolydayController {
    private static final String MODEL_MEMBER = "member";
    private static final String MODEL_ERROR = "error";

    // TODO SpringSecurity System, Administrator, Operator , Inspector 권한 필요
    private static final String AUTH_TYPES = "authrityTypes";

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private HolydayService holydayService;

    private ExtensibleModelMapper extensibleModelMapper = new ExtensibleModelMapper();

    @PostConstruct
    private void setUp() {
        extensibleModelMapper.setModelMapper(modelMapper);
    }


    @RequestMapping(value = {"/holyday", "/holyday/{year}"}, method = RequestMethod.GET)
    public String getHolyday(Model model, @PathVariable Optional<String> year, KeywordCondition condition, PageStatus pageStatus) {
    	String y = DateUtil.getCurrentDate("yyyy");
    	if(year.isPresent())
    		y = year.get();
    	
    	//현재일자 리터 
    	model.addAttribute("today", y+"-01-01");
    	model.addAttribute("currentYear",y);
    	ObjectMapper mapper = new ObjectMapper();

    	//올해휴일 리스트
    	try {
    		List<Holyday> holydays = holydayService.findHolydaysByYear(Integer.parseInt(y));
			model.addAttribute("holydays", mapper.writeValueAsString(holydays));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return "settings/holyday";
    }
    
    @RequestMapping(value = "/holyday/reset/{year}", method = RequestMethod.GET)
    public String setHolydayReset(Model model, @PathVariable int year,  KeywordCondition condition, PageStatus pageStatus) {
    	
    	Holyday holyday = new Holyday();
    	holyday.setYear(year);
    	holydayService.deleteYear(holyday);
    	List<Holyday> holydays = holydayService.reset(year);
    	
    	model.addAttribute("holydays", holydays);
        return "settings/holyday";
    }
    
    @RequestMapping(value = "/holyday/edit", method = RequestMethod.PUT)
    public ResponseEntity<HashMap<String, String>> setHolydayEdit(Model model,  @Valid Holyday request,  KeywordCondition condition, PageStatus pageStatus) {
    	 
    	Holyday holyday = holydayService.save(request);
    	//model.addAttribute("holyday", holyday);
    	HashMap<String, String> requestMap = new HashMap<String, String>();
    	requestMap.put("id", holyday.getId()+"");
    	requestMap.put("date", holyday.getDate());
        return ResponseEntity.status(HttpStatus.OK).body(requestMap);
    }
    
    @RequestMapping(value = "/holyday/delete", method = RequestMethod.PUT)
    public ResponseEntity deleteHolyday(Model model,  @Valid Holyday request,  KeywordCondition condition, PageStatus pageStatus) {
    	 
    	holydayService.delete(request);
    	return ResponseEntity.status(HttpStatus.OK).build();
    }

    public void jvm8security() {
        List<String> f = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API"); 
        f.forEach(n -> System.out.println(n));
    }

}