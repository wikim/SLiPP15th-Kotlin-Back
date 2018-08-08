package net.slipp.fifth.kotlin.dataroom.volunteer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVolunteerLog is a Querydsl query type for VolunteerLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVolunteerLog extends EntityPathBase<VolunteerLog> {

    private static final long serialVersionUID = 4390144L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVolunteerLog volunteerLog = new QVolunteerLog("volunteerLog");

    public final net.slipp.fifth.kotlin.common.QAuditableUJBLIBEntity _super;

    // inherited
    public final net.slipp.fifth.kotlin.member.QMember createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate;

    public final StringPath EtcInform = createString("EtcInform");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    // inherited
    public final net.slipp.fifth.kotlin.member.QMember lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate;

    public final StringPath pDate = createString("pDate");

    public final StringPath pYear = createString("pYear");

    public final NumberPath<Integer> UserManVolunteer = createNumber("UserManVolunteer", Integer.class);

    public final NumberPath<Integer> UserWomanVolunteer = createNumber("UserWomanVolunteer", Integer.class);

    public final NumberPath<Integer> UserYouthBoyVolunteer = createNumber("UserYouthBoyVolunteer", Integer.class);

    public final NumberPath<Integer> UserYouthGirlVolunteer = createNumber("UserYouthGirlVolunteer", Integer.class);

    public final DateTimePath<java.util.Date> wDate = createDateTime("wDate", java.util.Date.class);

    public QVolunteerLog(String variable) {
        this(VolunteerLog.class, forVariable(variable), INITS);
    }

    public QVolunteerLog(Path<? extends VolunteerLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVolunteerLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVolunteerLog(PathMetadata metadata, PathInits inits) {
        this(VolunteerLog.class, metadata, inits);
    }

    public QVolunteerLog(Class<? extends VolunteerLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new net.slipp.fifth.kotlin.common.QAuditableUJBLIBEntity(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.lastModifiedBy = _super.lastModifiedBy;
        this.lastModifiedDate = _super.lastModifiedDate;
    }

}

