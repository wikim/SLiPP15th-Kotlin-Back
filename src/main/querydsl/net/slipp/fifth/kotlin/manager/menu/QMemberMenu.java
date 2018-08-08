package net.slipp.fifth.kotlin.manager.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberMenu is a Querydsl query type for MemberMenu
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberMenu extends EntityPathBase<MemberMenu> {

    private static final long serialVersionUID = -1212711680L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberMenu memberMenu = new QMemberMenu("memberMenu");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final BooleanPath createYn = createBoolean("createYn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastModifiedDate = createDateTime("lastModifiedDate", java.util.Date.class);

    public final net.slipp.fifth.kotlin.member.QMember member;

    public final NumberPath<Long> menuId = createNumber("menuId", Long.class);

    public final BooleanPath printYn = createBoolean("printYn");

    public final BooleanPath readYn = createBoolean("readYn");

    public final BooleanPath updateYn = createBoolean("updateYn");

    public QMemberMenu(String variable) {
        this(MemberMenu.class, forVariable(variable), INITS);
    }

    public QMemberMenu(Path<? extends MemberMenu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberMenu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberMenu(PathMetadata metadata, PathInits inits) {
        this(MemberMenu.class, metadata, inits);
    }

    public QMemberMenu(Class<? extends MemberMenu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new net.slipp.fifth.kotlin.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

