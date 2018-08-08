package net.slipp.fifth.kotlin.service.holyday;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QHolyday is a Querydsl query type for Holyday
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QHolyday extends EntityPathBase<Holyday> {

    private static final long serialVersionUID = 1118537058L;

    public static final QHolyday holyday = new QHolyday("holyday");

    public final StringPath date = createString("date");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QHolyday(String variable) {
        super(Holyday.class, forVariable(variable));
    }

    public QHolyday(Path<? extends Holyday> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHolyday(PathMetadata metadata) {
        super(Holyday.class, metadata);
    }

}

