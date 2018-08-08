package net.slipp.fifth.kotlin.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${info.build.version}")
    private String buildVersion;

    /**
     * Sign-in Member
     *
     * @return
     */
    @RequestMapping(value = "/sign-in")
    public String signin(@RequestParam(required = false) String error, Model model) {
        if (StringUtils.hasText(error)) {
            model.addAttribute("error", error);
        }
        model.addAttribute("applicationName", applicationName);
        model.addAttribute("buildVersion", buildVersion);
        return "sign-in";
    }
    	
    @RequestMapping(value = "/current-date-time")
    public ResponseEntity<?> getCurrentDateTime(@RequestParam(required = false) String error, Model model) {
    	Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        Map<String, Object> map = new HashMap<>();
        map.put("date", dateFormat.format(date));
        map.put("time", timeFormat.format(date));
        
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    
}
