package net.slipp.fifth.kotlin.manager.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMenuItem is a Querydsl query type for MenuItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMenuItem extends EntityPathBase<MenuItem> {

    private static final long serialVersionUID = 1304416441L;

    public static final QMenuItem menuItem = new QMenuItem("menuItem");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastModifiedDate = createDateTime("lastModifiedDate", java.util.Date.class);

    public final StringPath menuName = createString("menuName");

    public final StringPath path = createString("path");

    public final StringPath subMenuName = createString("subMenuName");

    public QMenuItem(String variable) {
        super(MenuItem.class, forVariable(variable));
    }

    public QMenuItem(Path<? extends MenuItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuItem(PathMetadata metadata) {
        super(MenuItem.class, metadata);
    }

}

