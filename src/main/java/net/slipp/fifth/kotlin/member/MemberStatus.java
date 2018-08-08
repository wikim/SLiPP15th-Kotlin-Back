package net.slipp.fifth.kotlin.member;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.Getter;
import lombok.ToString;
import net.slipp.fifth.kotlin.common.CodeableEnum;

@Getter
@ToString
public enum MemberStatus implements CodeableEnum {
    /**
     * Un Approval: 미승인상태
     */
    DISAPPROVAL("disapproval", "code.memberStatus.unApproval", 1),
    /**
     * Active: 활성상태
     */
    ACTIVE("active", "code.memberStatus.active", 2),
    /**
     * Stop: 중지상태
     */
    STOP("stop", "code.memberStatus.stop", 3),
    /**
     * Leave: 탈퇴상태
     */
    LEAVE("leave", "code.memberStatus.leave", 4);

    private String code;
    private String key;
    private int order;

    MemberStatus(String code, String key, int order) {
        this.code = code;
        this.key = key;
        this.order = order;
    }

    public MemberStatus typeFromOrder(int order) {
        for (MemberStatus status : values()) {
            if (status.getOrder() == order) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown order(" + order + ") for EnumType"
                + MemberStatus.class.getSimpleName());
    }

    /**
     * order에 따라 정렬된 집합을 던질 것인지 무작위로 반환할 것인지
     *
     * @param isSort
     *            (true: sort/ false: unsort)
     * @return
     */
    public static Set<MemberStatus> types(boolean isSort) {
        Set<MemberStatus> types = Sets.newTreeSet();
        if (isSort) {
            for (MemberStatus authority : values()) {
                types.add(authority);
            }
        } else {
            types = Sets.newHashSet(values());
        }
        return types;
    }
}
