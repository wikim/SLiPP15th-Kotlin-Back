package net.slipp.fifth.kotlin.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuditableUJBLIBEntity is a Querydsl query type for AuditableUJBLIBEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAuditableUJBLIBEntity extends EntityPathBase<AuditableUJBLIBEntity> {

    private static final long serialVersionUID = -2136988972L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuditableUJBLIBEntity auditableUJBLIBEntity = new QAuditableUJBLIBEntity("auditableUJBLIBEntity");

    public final QAbstractUJBLIBAuditable _super = new QAbstractUJBLIBAuditable(this);

    public final net.slipp.fifth.kotlin.member.QMember createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final net.slipp.fifth.kotlin.member.QMember lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public QAuditableUJBLIBEntity(String variable) {
        this(AuditableUJBLIBEntity.class, forVariable(variable), INITS);
    }

    public QAuditableUJBLIBEntity(Path<? extends AuditableUJBLIBEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuditableUJBLIBEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuditableUJBLIBEntity(PathMetadata metadata, PathInits inits) {
        this(AuditableUJBLIBEntity.class, metadata, inits);
    }

    public QAuditableUJBLIBEntity(Class<? extends AuditableUJBLIBEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new net.slipp.fifth.kotlin.member.QMember(forProperty("createdBy"), inits.get("createdBy")) : null;
        this.lastModifiedBy = inits.isInitialized("lastModifiedBy") ? new net.slipp.fifth.kotlin.member.QMember(forProperty("lastModifiedBy"), inits.get("lastModifiedBy")) : null;
    }

}

