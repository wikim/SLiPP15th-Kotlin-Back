package net.slipp.fifth.kotlin.system.attachefile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAttacheFile is a Querydsl query type for AttacheFile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAttacheFile extends EntityPathBase<AttacheFile> {

    private static final long serialVersionUID = -1492521326L;

    public static final QAttacheFile attacheFile = new QAttacheFile("attacheFile");

    public final StringPath contentType = createString("contentType");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final StringPath fileId = createString("fileId");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath path = createString("path");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public QAttacheFile(String variable) {
        super(AttacheFile.class, forVariable(variable));
    }

    public QAttacheFile(Path<? extends AttacheFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAttacheFile(PathMetadata metadata) {
        super(AttacheFile.class, metadata);
    }

}

