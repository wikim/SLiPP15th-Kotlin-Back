package net.slipp.fifth.kotlin.member;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.BooleanBuilder;

import javassist.NotFoundException;
import net.slipp.fifth.kotlin.member.QMember;
import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.KeywordCondition;
import net.slipp.fifth.kotlin.common.mapper.ExtensibleModelMapper;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.configuration.ApplicationProperties;
import net.slipp.fifth.kotlin.member.dto.MemberDto;
import net.slipp.fifth.kotlin.member.dto.MemberDto.Request;
import net.slipp.fifth.kotlin.member.dto.MemberDto.Response;
import net.slipp.fifth.kotlin.member.typ.MemberAuthority;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFile;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFileService;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository repository;
    @Autowired
    private AttacheFileService attacheFileService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder PasswordEncoder;
    @Resource
    private ApplicationProperties ujblibProperties;

    private ExtensibleModelMapper extensibleModelMapper = new ExtensibleModelMapper();

    @PostConstruct
    private void setUp() {
        extensibleModelMapper.setModelMapper(modelMapper);
    }

    @Override
    public Member findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Member findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Member findBySigninId(String memberId) {
        return repository.findBySigninId(memberId);
    }

    @Override
    public Page<Member> search(KeywordCondition condition, PageStatus pageStatus) {
        QMember member = QMember.member;

        BooleanBuilder predicate = new BooleanBuilder();
        if (condition.hasKeyword()) {
            String likeKeyword = new StringBuilder().append("%").append(condition.getKeyword()).append("%").toString();
            predicate.and(member.signinId.like(likeKeyword).or(member.name.like(likeKeyword)));
        }

        Page<Member> page = repository.findAll(predicate, pageStatus);

        return page;
    }

    // TODO settings : members 의 datatable 형식을 위한 Method
    @Override
    public List<Response> searchMember(MemberCondition condition) {
        QMember qMember = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        if (condition.hasSigninId()) {
            builder.and(qMember.signinId.contains(condition.getSigninId()));
        }
        if (condition.hasName()) {
            builder.and(qMember.name.contains(condition.getName()));
        }
        
        List<Member> members = (List<Member>) repository.findAll(builder);
        return extensibleModelMapper.map(members, MemberDto.Response.class);
    }

    @Override
    public List<Member> searchAvailableProjectMember(String target) {
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(member.status.notIn(MemberStatus.DISAPPROVAL, MemberStatus.LEAVE));
        /*builder.and(member.authorities.any().in(MemberAuthority.PROJECT_MANAGER, MemberAuthority.OPERATOR,MemberAuthority.INSPECTOR));*/

        if (StringUtils.hasText(target)) {
            String likeWord = new StringBuilder().append("%").append(target).append("%").toString();
            builder.and(member.name.like(likeWord).or(member.nickName.like(likeWord).or(member.email.like(likeWord))));
        }

        return Lists.newArrayList(repository.findAll(builder));
    }

    @Transactional
    @Override
    public Member create(MemberDto.Request request) throws FileNotFoundException, IOException {
        Member member = modelMapper.map(request, Member.class);
        if(CollectionUtils.isEmpty(member.getAuthorities())) {
        	member.setAuthorities(Sets.newHashSet(MemberAuthority.MANAGER));
        }
        changePassword(member, request.getPassword());
        return save(member);
    }

    private void setDefaultProfileImage(Member member) throws IOException, FileNotFoundException {
        AttacheFile defaultProfileImage = attacheFileService.saveFileToTempoararyPath("default-profile.png",
                "image/png", new FileInputStream(ResourceUtils.getFile("classpath:static/images/default-profile.png")));
        attacheFileService.moveTempFileToDefinePath(member.getProfileImageDirectoryPath(), defaultProfileImage);
        member.setProfileImage(defaultProfileImage);
    }

    @Override
    public List<Member> findByAuthority(final MemberAuthority authority) {
        List<Member> members = repository.findAll();
        List<Member> administrators = Lists.newArrayList();
        for (Member member : members) {
            if (member.hasAuthority(authority)) {
                administrators.add(member);
            }
        }
        return administrators;
    }

    @Override
    @Transactional
    public Member modify(Member member, Request request) {
    	request.setPassword(member.getPassword());
        modelMapper.map(request, member);
        
        if(Objects.nonNull(request.getAuthorities()) && request.getAuthorities().size() > 0) {
        	member.setAuthorities(request.getAuthorities());
        }
        
        if(Objects.nonNull(request.getStatus())) {
        	member.setStatus(request.getStatus());
        }
        
        changePassword(member, request.getPassword());
        return repository.save(member);
    }

    @Override
    @Transactional
    public Member passwordChange(Member member) {
    	changePassword(member, member.getPassword());
        return save(member);
    }
    @Override
    @Transactional
    public Member modifyProfile(Member member, Request request) {
        modelMapper.map(request, member);
        changePassword(member, request.getPassword());
        return save(member);
    }

    private Member changePassword(Member member, String password) {
        if (StringUtils.hasText(password)) {
            member.changePassword(PasswordEncoder.encode(password));
        }
        return member;
    }

    @Override
    @Transactional
    public Member leaveMember(Member member) {
       
        member.leave();
        return repository.save(member);
    }

    @Override
    @Transactional
    public Member uploadProfileImage(Member member, AttacheFile profileImage) throws IOException {
        attacheFileService.moveTempFileToDefinePath(member.getProfileImageDirectoryPath(), profileImage);
        member.setProfileImage(profileImage);
        return save(member);
    }

    @Override
    @Transactional
    public Member deleteProfileImage(Member member) throws IOException {
        member.setProfileImage(null);
        return save(member);
    }

    @Override
    public boolean existByNameAndEmail(String name, String email) {
        return null != repository.findByNameAndEmail(name, email);
    }

    @Override
    public Member findByNameAndEmail(String name, String email) {
        return repository.findByNameAndEmail(name, email);
    }

    @Override
    @Transactional
    public Member save(Member member) {
        return repository.saveAndFlush(member);
    }
    
    @Override
    @Transactional
    public Member saveMember(Member member) {
        return repository.saveAndFlush(member);
    }

    private String makeToken(String name, String email) {
        String uuid = UUID.randomUUID().toString();
        String rawPassword = new StringBuffer().append(name).append(email).append(uuid).toString();
        return PasswordEncoder.encode(rawPassword);
    }

    /**
     * Change MemberStatus to {@link MemberStatus#STOP}
     *
     * @param name
     * @param email
     * @return
     * @throws NotFoundException
     */
    @Transactional
    private Member changeMemberStatusToStop(String name, String email) throws NotFoundException {
        Member member = findByNameAndEmail(name, email);
        if (null == member) {
            throw new NotFoundException("system.exception.notFoundMember");
        }
        member.stop();
        return save(member);
    }

    @Override
    public List<Member> getAllMember() {
        return repository.findAll();
    }

	@Override
	public boolean existByProjectAuthority(Member member, Request request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(Member member) {
		repository.delete(member);
		
	}
}
