package net.slipp.fifth.kotlin.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractUJBLIBAuditable is a Querydsl query type for AbstractUJBLIBAuditable
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractUJBLIBAuditable extends EntityPathBase<AbstractUJBLIBAuditable<?, ? extends java.io.Serializable>> {

    private static final long serialVersionUID = 749779935L;

    public static final QAbstractUJBLIBAuditable abstractUJBLIBAuditable = new QAbstractUJBLIBAuditable("abstractUJBLIBAuditable");

    public final org.springframework.data.jpa.domain.QAbstractPersistable _super = new org.springframework.data.jpa.domain.QAbstractPersistable(this);

    public final SimplePath<Object> createdBy = createSimple("createdBy", Object.class);

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    public final SimplePath<Object> lastModifiedBy = createSimple("lastModifiedBy", Object.class);

    public final DateTimePath<java.util.Date> lastModifiedDate = createDateTime("lastModifiedDate", java.util.Date.class);

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAbstractUJBLIBAuditable(String variable) {
        super((Class) AbstractUJBLIBAuditable.class, forVariable(variable));
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAbstractUJBLIBAuditable(Path<? extends AbstractUJBLIBAuditable> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAbstractUJBLIBAuditable(PathMetadata metadata) {
        super((Class) AbstractUJBLIBAuditable.class, metadata);
    }

}

