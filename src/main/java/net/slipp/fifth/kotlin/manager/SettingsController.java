package net.slipp.fifth.kotlin.manager;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.KeywordCondition;
import net.slipp.fifth.kotlin.common.mapper.ExtensibleModelMapper;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.common.util.ObjectMapperUtils;
import net.slipp.fifth.kotlin.manager.menu.MemberMenu;
import net.slipp.fifth.kotlin.manager.menu.MemberMenuDto;
import net.slipp.fifth.kotlin.manager.menu.MemberMenuService;
import net.slipp.fifth.kotlin.manager.menu.MenuItem;
import net.slipp.fifth.kotlin.manager.menu.MenuItemService;
import net.slipp.fifth.kotlin.member.Member;
import net.slipp.fifth.kotlin.member.MemberService;
import net.slipp.fifth.kotlin.member.dto.MemberDto;
import net.slipp.fifth.kotlin.member.dto.UserInfoDto;
import net.slipp.fifth.kotlin.member.typ.MemberAuthority;
import net.slipp.fifth.kotlin.web.support.pagination.Paginations;

@Slf4j
@Controller
@RequestMapping("/settings")
public class SettingsController {
    private static final String MODEL_MEMBER = "member";
    private static final String MODEL_ERROR = "error";

    // TODO SpringSecurity System, Administrator, Operator , Inspector 권한 필요
    private static final String AUTH_TYPES = "authrityTypes";

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMenuService memberMenuService;
    @Autowired
    private MenuItemService menuItemService;

    @Value("${spring.datasource.hikari.jdbc-url}")
    private String url;
    @Value("${spring.datasource.hikari.username}")
    private String username;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    private ExtensibleModelMapper extensibleModelMapper = new ExtensibleModelMapper();

    @PostConstruct
    private void setUp() {
        extensibleModelMapper.setModelMapper(modelMapper);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String settings(Model model) {
        model.addAttribute("url", url);
        model.addAttribute("username", username);
        model.addAttribute("driverClassName", driverClassName);
        
        return "settings/main";
    }

    @RequestMapping(value = "/members")
    public String getMembers(Model model, KeywordCondition condition, PageStatus pageStatus) {
        model.addAttribute("page", Paginations.pagination(memberService.search(condition, pageStatus)));
        return "settings/members";
    }
    
    @RequestMapping(value = "/member/create")
    public String memberCreate(Model model, KeywordCondition condition, PageStatus pageStatus) {
        model.addAttribute("page", Paginations.pagination(memberService.search(condition, pageStatus)));
        Member member = new Member();
        member.setPassword("ujblib123!1");
        
        model.addAttribute(AUTH_TYPES, MemberAuthority.values());
        model.addAttribute("member",member);
        
        return "settings/member-create";
    }

    @RequestMapping(value = "/member/save", method = RequestMethod.PUT)
    public String memberSave( @Valid MemberDto.Request request,
            BindingResult bindingResult, Model model, Member sessionMember,
            KeywordCondition condition, PageStatus pageStatus) {
        try {
        	request.getAuthorities();
        	request.setEmail("-");
        	request.setCellPhoneNumber("-");
        	request.setInPhoneNumber("-");
        	Member member = memberService.create(request);
        	List<MenuItem> items = menuItemService.findAll();
    		if(CollectionUtils.isEmpty(member.getMenus())) {
    			for(MenuItem item : items) {
    				MemberMenu menu = new MemberMenu();
    				menu.setMember(member);
    				menu.setMenuId(item.getId());
    				memberMenuService.save(menu);
    			}
    		}
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
        model.addAttribute("page", Paginations.pagination(memberService.search(condition, pageStatus)));
        return "settings/members";
    }
    
    @RequestMapping(value = "/member/modify/{member}")
    public String mergeMember( @PathVariable Member member, @Valid MemberDto.Request request,
            BindingResult bindingResult, Model model, Member sessionMember,
            KeywordCondition condition, PageStatus pageStatus) {
        	//Member memberUp = modelMapper.map(request, Member.class);
        	//memberService.save(memberUp);
    	request.setEmail("-");
    	request.setCellPhoneNumber("-");
    	request.setInPhoneNumber("-");
    	request.setAuthorities((Set<MemberAuthority>) member.getAuthorities());
    	memberService.modify(member, request);
        model.addAttribute("page", Paginations.pagination(memberService.search(condition, pageStatus)));
        return "settings/members";
    }
    
    @RequestMapping(value = "/member/password", method = RequestMethod.PUT)
    public String memberPasswordChange( @Valid MemberDto.Request request,
            BindingResult bindingResult, Model model, Member sessionMember,
            KeywordCondition condition, PageStatus pageStatus) {
    	
		Member member = memberService.findById(request.getId());
		member.setPassword(request.getPassword());
		memberService.passwordChange(member);
		
        model.addAttribute("page", Paginations.pagination(memberService.search(condition, pageStatus)));
        return "settings/members";
    }
    
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> passwordChange(@RequestBody UserInfoDto dto, Model model) {
    	Map<String, Object> result = Maps.newHashMap();
    	try {
    		Member member = memberService.findBySigninId(dto.getUserId());
    		member.setPassword(dto.getPassword());
    		memberService.passwordChange(member);
			result.put("result", Boolean.TRUE);
		} catch(Exception e) {
			log.error("loginUserPasswordChange Exception = {}", e);
			result.put("result", Boolean.FALSE);
		}
		return result;
    }
    
    @RequestMapping(value = "/members/{member}", method = RequestMethod.GET)
    public String getMember(@PathVariable Member member, Model model) {
    	member = memberService.findById(member.getId());
        model.addAttribute("member",member);
        return "settings/member-modify";
    }

    @RequestMapping(value = "/members/{member}", method = RequestMethod.PUT)
    public String modifyMember(@PathVariable Member member, @Valid MemberDto.Request request,
            BindingResult bindingResult, Model model, Member sessionMember) {
        if (bindingResult.hasErrors()) {
            return "settings/member-modify";
        }
        if(memberService.existByProjectAuthority(member, request)) {
        	model.addAttribute(MODEL_ERROR, Boolean.TRUE);
        	model.addAttribute(MODEL_MEMBER, member);
            model.addAttribute(AUTH_TYPES, MemberAuthority.values());
    		return "settings/member-modify";
        }
        
        member.setLastModifiedBy(sessionMember);
        member.setLastModifiedDate(new Date());
        memberService.modify(member, request);
        return "redirect:/settings/members";
    }
    
    @RequestMapping(value = "/member/delete/{member}", method = RequestMethod.PUT)
    public String deleteMember(@PathVariable Member member, @Valid MemberDto.Request request,
            BindingResult bindingResult, Model model, Member sessionMember) {
    	memberService.delete(member);
        return "redirect:/settings/members";
    }
    
    @RequestMapping(value = {"/menu-auth" }, method = RequestMethod.GET)
    public String menuAuth(Model model, @RequestParam(required = true) Long id) {
		Member member = memberService.findById(id);
		List<MenuItem> items = menuItemService.findAll();
		if(CollectionUtils.isEmpty(member.getMenus())) {
			for(MenuItem item : items) {
				MemberMenu menu = new MemberMenu();
				menu.setMember(member);
				menu.setMenuId(item.getId());
				memberMenuService.save(menu);
			}
		}
		List<MemberMenuDto> menus = Lists.newArrayList();
		menus = ObjectMapperUtils.mapAll(member.getMenus(), MemberMenuDto.class);
		menus.stream().forEach(x -> { 
										x.setMenuName(items.stream().filter(item -> item.getId().equals(x.getMenuId())).findAny().get().getMenuName());
										x.setSubMenuName(items.stream().filter(item -> item.getId().equals(x.getMenuId())).findAny().get().getSubMenuName());
									});
		model.addAttribute("memberId", id);
		model.addAttribute("menus", menus);
		
		return "settings/menu-auth";
	}
    
    
    @RequestMapping(value = {"/menu-auth/init" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> initMenuAuth(@RequestParam(name = "id") Long id) {
		Map<String, Object> result = Maps.newHashMap();
		if(Objects.nonNull(id)) {
			Member member = memberService.findById(id);
			List<MenuItem> items = menuItemService.findAll();
			if(CollectionUtils.isEmpty(member.getMenus())) {
				for(MenuItem item : items) {
					MemberMenu menu = new MemberMenu();
					menu.setMember(member);
					menu.setMenuId(item.getId());
					memberMenuService.save(menu);
				}
			}
		}
		result.put("result", Boolean.TRUE);
		return result;
	}
    
    @RequestMapping(value = {"/menu-auth" }, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> registMemberMenu(@RequestBody List<MemberMenuDto> list, Model model) {
    	Map<String, Object> result = Maps.newHashMap();
		try {
			for(MemberMenuDto dto : list) {
				MemberMenu entity = memberMenuService.findOne(dto.getId());
				entity.setReadYn(dto.isReadYn());
				entity.setPrintYn(dto.isPrintYn());
				entity.setCreateYn(dto.isCreateYn());
				entity.setUpdateYn(dto.isUpdateYn());
				memberMenuService.save(entity);
			}
			result.put("result", Boolean.TRUE);
		} catch(Exception e) {
			log.error(e.getMessage());
			result.put("result", Boolean.FALSE);
		}
		return result;
	}
}