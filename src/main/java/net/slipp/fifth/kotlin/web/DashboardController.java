package net.slipp.fifth.kotlin.web;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.mapper.ExtensibleModelMapper;
import net.slipp.fifth.kotlin.dataroom.volunteer.VolunteerLogService;
import net.slipp.fifth.kotlin.manager.menu.MemberMenuService;
import net.slipp.fifth.kotlin.member.Member;
import net.slipp.fifth.kotlin.service.holyday.HolydayService;

@Slf4j
@Controller
public class DashboardController {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private HolydayService holydayService;

    @Autowired
    private VolunteerLogService volunteerLogService;

    @Autowired
    private MemberMenuService memberMenuService;

    private ExtensibleModelMapper extensibleModelMapper;
    
    @PostConstruct
    public void setUp() {
        Assert.notNull(modelMapper, "modelMapper is not null.");
        extensibleModelMapper = new ExtensibleModelMapper();
        extensibleModelMapper.setModelMapper(modelMapper);
    }

    @RequestMapping(value = { "/", "/index", "/dashboard" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public String showMain(Model model, Member member,
    		@RequestParam(required = false, name="pYear") String pYear,
    		HttpSession session) {
    	log.info(member.toString());
		session.setAttribute("menus", member.getMenus());
		log.info(Objects.nonNull(session.getAttribute("menus")) ? session.getAttribute("menus").toString() : "");
        return "index";
    }
    
}
