package net.slipp.fifth.kotlin.member;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import net.slipp.fifth.kotlin.common.KeywordCondition;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.member.dto.MemberDto;
import net.slipp.fifth.kotlin.member.dto.MemberDto.Request;
import net.slipp.fifth.kotlin.member.typ.MemberAuthority;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFile;

public interface MemberService {
    /**
     * Search Member by Search Condition
     *
     * @param condition
     *            {@link KeywordCondition}
     * @param pageStatus
     *            {@link PageStatus}
     * @return {@link Pagination<MemberDto.Response>}
     */
    public Page<Member> search(KeywordCondition condition, PageStatus pageStatus);

    /**
     * Find Member by signinId
     *
     * @param signinId
     * @return Matched Member by signinId or null
     */
    public Member findBySigninId(String signinId);

    /**
     * Find Member by email
     *
     * @param email
     *            {@link String} unique Email
     * @return
     */
    public Member findByEmail(String email);

    /**
     * {@link Member#getId()} 를 이용하여 사용자 탐색
     * 
     * @param id
     *            Long type
     * @return
     */
    public Member findById(Long id);

    /**
     * Create new member
     *
     * @param request
     *            {@link Member}
     * @return saved new {@link Member}
     * @throws IOException
     * @throws FileNotFoundException
     */
    public Member create(MemberDto.Request request) throws FileNotFoundException, IOException;

    /**
     * Modify member's info.
     *
     * @param member
     * @param memberDto
     * @return
     */
    public Member modify(Member member, MemberDto.Request request);

    public Member passwordChange(Member member);

    /**
     * Search available Project Members
     *
     * @param target
     *            (name, nickName, email)
     * @return List<Member> members
     */
    public List<Member> searchAvailableProjectMember(String target);

    /**
     * find members has {@link MemberAuthority}
     *
     * don't use open api.
     *
     * @param authority
     *            {@link MemberAuthority}
     * @return
     */
    public List<Member> findByAuthority(MemberAuthority authority);

    /**
     * Modify Profile ignore Member.status, MemberAuthorities
     *
     * @param member
     * @param request
     * @return
     */
    public Member modifyProfile(Member member, Request request);

    /**
     * 회원 탈퇴처리
     * 
     * @param member
     * @return
     */
    public Member leaveMember(Member member);

    /**
     * {@link MemberCondition}을 이용하여 회원정보 검색
     * 
     * @param condition
     * @return
     */
    List<MemberDto.Response> searchMember(MemberCondition condition);

    /**
     * Upload member's profile image
     *
     * @param member
     *            {@link Member}
     * @param profileImage
     *            {@link Member}
     * @return {@link Member}
     * @throws IOException
     *             file not found.
     */
    public Member uploadProfileImage(Member member, AttacheFile profileImage) throws IOException;

    /**
     * Remove member's profile image
     *
     * @param member
     *            {@link Member}
     * @return
     * @throws IOException
     *             file not found.
     */
    public Member deleteProfileImage(Member member) throws IOException;

    /**
     * 회원의 이름과 이메일을 이용하여 존재여부 확인
     * 
     * @param name
     * @param email
     * @return true: yes/ false: no
     */
    public boolean existByNameAndEmail(String name, String email);

    /**
     * 회원의 이름과 이메일을 이용하여 탐색
     * 
     * @param name
     * @param email
     * @return
     */
    public Member findByNameAndEmail(String name, String email);

    /**
     * 회원 저장
     * 
     * @param member
     * @return
     */
    public Member save(Member member);
    
    /**
     * External 회원 저장
     * @param member
     * @return
     */
    public Member saveMember(Member member);

    public List<Member> getAllMember();
    
    public boolean existByProjectAuthority(Member member, Request request);
    
    public void delete(Member member);
}
