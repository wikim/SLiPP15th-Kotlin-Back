package net.slipp.fifth.kotlin.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -684090905L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final SetPath<net.slipp.fifth.kotlin.member.typ.MemberAuthority, EnumPath<net.slipp.fifth.kotlin.member.typ.MemberAuthority>> authorities = this.<net.slipp.fifth.kotlin.member.typ.MemberAuthority, EnumPath<net.slipp.fifth.kotlin.member.typ.MemberAuthority>>createSet("authorities", net.slipp.fifth.kotlin.member.typ.MemberAuthority.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath cellPhoneNumber = createString("cellPhoneNumber");

    public final StringPath chargeRoom = createString("chargeRoom");

    public final QMember createdBy;

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final StringPath departmentNm = createString("departmentNm");

    public final StringPath email = createString("email");

    public final StringPath gradeNm = createString("gradeNm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inPhoneNumber = createString("inPhoneNumber");

    public final QMember lastModifiedBy;

    public final DateTimePath<java.util.Date> lastModifiedDate = createDateTime("lastModifiedDate", java.util.Date.class);

    public final ListPath<net.slipp.fifth.kotlin.manager.menu.MemberMenu, net.slipp.fifth.kotlin.manager.menu.QMemberMenu> menus = this.<net.slipp.fifth.kotlin.manager.menu.MemberMenu, net.slipp.fifth.kotlin.manager.menu.QMemberMenu>createList("menus", net.slipp.fifth.kotlin.manager.menu.MemberMenu.class, net.slipp.fifth.kotlin.manager.menu.QMemberMenu.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath nickName = createString("nickName");

    public final StringPath password = createString("password");

    public final net.slipp.fifth.kotlin.system.attachefile.QAttacheFile profileImage;

    public final StringPath signinId = createString("signinId");

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new QMember(forProperty("createdBy"), inits.get("createdBy")) : null;
        this.lastModifiedBy = inits.isInitialized("lastModifiedBy") ? new QMember(forProperty("lastModifiedBy"), inits.get("lastModifiedBy")) : null;
        this.profileImage = inits.isInitialized("profileImage") ? new net.slipp.fifth.kotlin.system.attachefile.QAttacheFile(forProperty("profileImage")) : null;
    }

}

