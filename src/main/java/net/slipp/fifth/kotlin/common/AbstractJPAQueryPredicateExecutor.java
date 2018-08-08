package net.slipp.fifth.kotlin.common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;


@Transactional(readOnly = true)
public abstract class AbstractJPAQueryPredicateExecutor<T> implements JPQLQueryPredicateExecutor<T> {

    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLER = SimpleEntityPathResolver.INSTANCE;

    private final Class<T> entityClass;
    private final EntityPath<T> path;
    private final PathBuilder<T> builder;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractJPAQueryPredicateExecutor() {
        this.entityClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(),
                AbstractJPAQueryPredicateExecutor.class);
        this.path = (EntityPath<T>) DEFAULT_ENTITY_PATH_RESOLER.createPath(entityClass);
        this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
    }

    @Override
    public List<T> findAll(JPQLQuery query) {
        return (List<T>) query.fetch();
    }

    @Override
    public Page<T> findAll(JPQLQuery query, Pageable pageable) {
        long count = query.fetchCount();
        applyPagination(query, pageable);
        return new PageImpl<T>(query.fetch(), pageable, count);
    }

    private JPQLQuery applyPagination(JPQLQuery query, Pageable pageable) {
        if (null == pageable) {
            return query;
        }

        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        return applySorting(query, pageable.getSort());
    }

    private JPQLQuery applySorting(JPQLQuery query, Sort sort) {
        if (null == sort) {
            return query;
        }

        for (Order order : sort) {
            query.orderBy(toOrder(order));
        }

        return query;
    }

    protected OrderSpecifier<?> toOrder(Order order) {
        Expression<Object> property = builder.get(order.getProperty());
        return new OrderSpecifier(order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, property);
    }

    /**
     * @see QueryDslJpaRepository#createQuery
     */
    @Override
    public JPQLQuery createQuery(Predicate... predicates) {
        return (JPQLQuery) new JPAQuery(entityManager).from(path).where(predicates);
    }

}
