package net.slipp.fifth.kotlin.member.typ;

import java.util.Set;

import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Sets;

import lombok.Getter;
import net.slipp.fifth.kotlin.common.CodeableEnum;

/**
 * 회원권한
 * 
 * 애플리케이션 내에서 웹접근권한을 가지게 된다.
 * 
 * @author jiheon
 *
 */
@Getter
public enum MemberAuthority implements Ordered, GrantedAuthority, CodeableEnum {
    ADMINISTRATOR("administrator", "시스템관리자", 1),

    /**
     * Project Manager(project, project member, jobs of project management)
     */
    MANAGER("manager", "운영자", 2),

    /**
     * Operator( operator jobs of project)
     */
    OPERATOR("operator", "일반회원", 3);

    private String code;
    private String key;
    private int order;

    MemberAuthority(String code, String key, int order) {
        this.code = code;
        this.key = key;
        this.order = order;
    }

    /**
     * 입력된 순서에 대한 Enum을 반환한다.
     *
     * @param order
     * @return matched {@link MemberAuthority}
     * @throws IllegalArgumentException
     *             등록되지 않은 순서가 입력되는 순간
     */
    public static MemberAuthority typeFormOrder(int order) throws IllegalArgumentException {
        for (MemberAuthority authority : values()) {
            if (authority.getOrder() == order) {
                return authority;
            }
        }

        throw new IllegalArgumentException("Unknown order(" + order + ") for EnumType"
                + MemberAuthority.class.getSimpleName());
    }

    /**
     * order에 따라 정렬된 집합을 던질 것인지 무작위로 반환할 것인지
     *
     * @param isSort
     *            (true: sort/ false: unsort)
     * @return
     */
    public static Set<MemberAuthority> types(boolean isSort) {
        Set<MemberAuthority> types = Sets.newTreeSet();
        if (isSort) {
            for (MemberAuthority authority : values()) {
                types.add(authority);
            }
        } else {
            types = Sets.newHashSet(values());
        }
        return types;
    }

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
